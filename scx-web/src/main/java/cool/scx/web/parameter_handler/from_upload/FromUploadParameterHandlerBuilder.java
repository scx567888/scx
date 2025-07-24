package cool.scx.web.parameter_handler.from_upload;

import com.fasterxml.jackson.databind.JavaType;
import cool.scx.http.media.multi_part.MultiPartPart;
import cool.scx.reflect.ParameterInfo;
import cool.scx.web.parameter_handler.ParameterHandler;
import cool.scx.web.parameter_handler.ParameterHandlerBuilder;

import static cool.scx.common.util.ObjectUtils.constructType;

/// 处理 FileUpload 类型参数
///
/// @author scx567888
/// @version 0.0.1
public final class FromUploadParameterHandlerBuilder implements ParameterHandlerBuilder {

    @Override
    public ParameterHandler tryBuild(ParameterInfo parameter) {
        // todo 这里可能有问题
        var javaType = constructType(parameter.rawParameter().getParameterizedType());
        var isArray = javaType.isCollectionLikeType() || javaType.isArrayType();
        var rawType = isArray ? javaType.getContentType().getRawClass() : javaType.getRawClass();
        if (rawType != MultiPartPart.class) {
            return null;
        }
        return new FromUploadParameterHandler(parameter);
    }

}
