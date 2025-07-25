package cool.scx.web.parameter_handler.from_upload;

import cool.scx.http.media.multi_part.MultiPartPart;
import cool.scx.reflect.ArrayTypeInfo;
import cool.scx.reflect.ParameterInfo;
import cool.scx.reflect.TypeInfo;
import cool.scx.web.annotation.FromUpload;
import cool.scx.web.parameter_handler.ParameterHandler;
import cool.scx.web.parameter_handler.RequestInfo;
import cool.scx.web.parameter_handler.exception.RequiredParamEmptyException;

import java.util.Collection;

import static cool.scx.common.constant.AnnotationValues.getRealValue;
import static cool.scx.object.ScxObject.convertValue;
import static java.util.Collections.addAll;

/// 处理 FileUpload 类型参数
///
/// @author scx567888
/// @version 0.0.1
public final class FromUploadParameterHandler implements ParameterHandler {

    private final boolean isCollection;
    private final boolean isArray;
    private final ParameterInfo parameter;
    private String value;
    private boolean required;

    public FromUploadParameterHandler(ParameterInfo parameter) {
        this.parameter = parameter;
        this.value = parameter.name();
        this.required = false;
        var fromUpload = parameter.findAnnotation(FromUpload.class);
        if (fromUpload != null) {
            var _value = getRealValue(fromUpload.value());
            if (_value != null) {
                value = _value;
            }
            required = fromUpload.required();
        }
        this.isCollection = checkIsCollection(parameter.parameterType());
        this.isArray = checkIsArray(parameter.parameterType());
    }

    // todo 这个正确吗?
    public static boolean checkIsCollection(TypeInfo typeInfo) {
        return Collection.class.isAssignableFrom(typeInfo.rawClass());
    }

    // todo 这个正确吗?
    public static boolean checkIsArray(TypeInfo typeInfo) {
        return typeInfo instanceof ArrayTypeInfo;
    }

    /// 从 RoutingContext 查找 对应名称的 上传对象 为空会返回 null
    ///
    /// @param routingContext a
    /// @param name           a
    /// @return a
    private static MultiPartPart[] findFileUploadListByName(RequestInfo routingContext, String name) {
        var fileUploads = routingContext.uploadFiles();
        return fileUploads.getAll(name).toArray(MultiPartPart[]::new);
    }

    @Override
    public Object handle(RequestInfo requestInfo) throws RequiredParamEmptyException {

        var v = findFileUploadListByName(requestInfo, value);

        //为空的时候做两个处理 即必填则报错 非必填则返回 null
        if (v.length == 0) {
            if (required) {
                throw new RequiredParamEmptyException("必填参数不能为空 !!! 参数名称 [" + value + "] , 参数来源 [FromUpload] , 参数类型 [" + parameter.parameterType() + "]");
            }
            return null;
        }

        if (isArray) {
            return v;
        }
        if (isCollection) {
            //这里我们无法确定具体的类型 所以使用 ObjectUtils 帮我们创建一个
            @SuppressWarnings("unchecked")
            var list = (Collection<Object>) convertValue(new Object[]{}, parameter.parameterType());// todo 这里正确吗? 
            addAll(list, v);
            return list;
        } else {
            return v[0];
        }
    }

}
