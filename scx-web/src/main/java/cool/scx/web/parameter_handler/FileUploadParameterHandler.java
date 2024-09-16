package cool.scx.web.parameter_handler;

import cool.scx.common.reflect.ParameterInfo;
import cool.scx.common.util.AnnotationUtils;
import cool.scx.web.annotation.FromUpload;
import cool.scx.web.parameter_handler.exception.RequiredParamEmptyException;
import cool.scx.web.type.FileUpload;

import java.util.Collection;

import static cool.scx.common.util.ObjectUtils.convertValue;
import static java.util.Collections.addAll;

/**
 * 处理 FileUpload 类型参数
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class FileUploadParameterHandler implements ParameterHandler {

    /**
     * 从 RoutingContext 查找 对应名称的 上传对象 为空会返回 null
     *
     * @param routingContext a
     * @param name           a
     * @return a
     */
    private static FileUpload[] findFileUploadListByName(RequestInfo routingContext, String name) {
        var fileUploads = routingContext.formData();
        var formDataParts = fileUploads.getAll(name);
        //todo 有 bug
        return formDataParts.stream().filter(f -> name.equals(f.name())).toArray(FileUpload[]::new);
    }

    @Override
    public boolean canHandle(ParameterInfo parameter) {
        var isArray = parameter.type().isCollectionLikeType() || parameter.type().isArrayType();
        var rawType = isArray ? parameter.type().getContentType().getRawClass() : parameter.type().getRawClass();
        return rawType == FileUpload.class;
    }

    @Override
    public Object handle(ParameterInfo parameter, RequestInfo requestInfo) throws RequiredParamEmptyException {

        var name = parameter.name();
        var required = false;
        var fromUpload = parameter.parameter().getAnnotation(FromUpload.class);
        if (fromUpload != null) {
            var _value = AnnotationUtils.getAnnotationValue(fromUpload.value());
            if (_value != null) {
                name = _value;
            }
            required = fromUpload.required();
        }

        var v = findFileUploadListByName(requestInfo, name);

        //为空的时候做两个处理 即必填则报错 非必填则返回 null
        if (v.length == 0) {
            if (required) {
                throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + name + "] , 参数来源 [FromUpload] , 参数类型 [" + parameter.type() + "]");
            }
            return null;
        }

        var isCollection = parameter.type().isCollectionLikeType();
        var isArray = parameter.type().isArrayType();

        if (isArray) {
            return v;
        }
        if (isCollection) {
            //这里我们无法确定具体的类型 所以使用 ObjectUtils 帮我们创建一个
            @SuppressWarnings("unchecked")
            var list = (Collection<Object>) convertValue(new Object[]{}, parameter.type());
            addAll(list, v);
            return list;
        } else {
            return v[0];
        }
    }

}
