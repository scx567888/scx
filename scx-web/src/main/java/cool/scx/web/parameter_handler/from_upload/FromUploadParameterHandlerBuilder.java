package cool.scx.web.parameter_handler.from_upload;

import cool.scx.http.media.multi_part.MultiPartPart;
import cool.scx.reflect.ParameterInfo;
import cool.scx.web.parameter_handler.ParameterHandler;
import cool.scx.web.parameter_handler.ParameterHandlerBuilder;

/**
 * 处理 FileUpload 类型参数
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class FromUploadParameterHandlerBuilder implements ParameterHandlerBuilder {

    @Override
    public ParameterHandler tryBuild(ParameterInfo parameter) {
        var isArray = parameter.type().isCollectionLikeType() || parameter.type().isArrayType();
        var rawType = isArray ? parameter.type().getContentType().getRawClass() : parameter.type().getRawClass();
        if (rawType != MultiPartPart.class) {
            return null;
        }
        return new FromUploadParameterHandler(parameter);
    }

}
