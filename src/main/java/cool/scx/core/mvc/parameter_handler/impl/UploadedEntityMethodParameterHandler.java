package cool.scx.core.mvc.parameter_handler.impl;

import cool.scx.core.annotation.FromUpload;
import cool.scx.core.mvc.parameter_handler.RequiredParamEmptyException;
import cool.scx.core.mvc.parameter_handler.ScxMappingMethodParameterHandler;
import cool.scx.core.mvc.parameter_handler.ScxMappingRoutingContextInfo;
import cool.scx.core.type.UploadedEntity;
import cool.scx.util.StringUtils;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;

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
     * 从 RoutingContext 查找 对应名称的 上传对象
     *
     * @param routingContext a
     * @param name           a
     * @return a
     */
    private static FileUpload findFileUploadByName(RoutingContext routingContext, String name) {
        var fileUploads = routingContext.fileUploads();
        for (var f : fileUploads) {
            if (name.equals(f.name())) {
                return f;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHandle(Parameter parameter) {
        var type = parameter.getParameterizedType();
        return type == UploadedEntity.class || type == FileUpload.class;
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
            if (StringUtils.notBlank(fromUpload.value())) {
                name = fromUpload.value();
            }
            required = fromUpload.required();
        }

        var v = findFileUploadByName(context.routingContext(), name);
        //为空的时候做两个处理 即必填则报错 非必填则返回 null
        if (required && v == null) {
            throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromUpload] , 参数类型 [" + parameter.getParameterizedType().getTypeName() + "]");
        }

        return parameter.getParameterizedType() == UploadedEntity.class && v != null ? new UploadedEntity(v) : v;
    }

}
