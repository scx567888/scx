package cool.scx.mvc.parameter_handler;

import cool.scx.common.util.reflect.AnnotationUtils;
import cool.scx.mvc.ScxMvcParameterHandler;
import cool.scx.mvc.ScxMvcRequestInfo;
import cool.scx.mvc.annotation.FromUpload;
import cool.scx.mvc.parameter_handler.exception.RequiredParamEmptyException;
import cool.scx.mvc.type.UploadedEntity;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.Parameter;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class UploadedEntityParameterHandler implements ScxMvcParameterHandler {

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
    public Object handle(Parameter parameter, ScxMvcRequestInfo info) throws RequiredParamEmptyException {
        var javaType = parameter.getParameterizedType();
        var name = parameter.getName();
        var required = false;
        var fromUpload = parameter.getAnnotation(FromUpload.class);
        if (fromUpload != null) {
            var _value = AnnotationUtils.getAnnotationValue(fromUpload.value());
            if (_value != null) {
                name = _value;
            }
            required = fromUpload.required();
        }

        var v = findFileUploadByName(info.routingContext(), name);
        //为空的时候做两个处理 即必填则报错 非必填则返回 null
        if (v == null && required) {
            throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromUpload] , 参数类型 [" + parameter.getParameterizedType().getTypeName() + "]");
        }

        return javaType == UploadedEntity.class && v != null ? new UploadedEntity(v) : v;
    }

}
