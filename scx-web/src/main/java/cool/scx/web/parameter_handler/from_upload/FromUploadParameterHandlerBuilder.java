package cool.scx.web.parameter_handler.from_upload;

import cool.scx.http.media.multi_part.MultiPartPart;
import dev.scx.reflect.ArrayTypeInfo;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.ParameterInfo;
import dev.scx.reflect.TypeInfo;
import cool.scx.web.parameter_handler.ParameterHandler;
import cool.scx.web.parameter_handler.ParameterHandlerBuilder;

import java.util.Collection;

/// 处理 FileUpload 类型参数
///
/// @author scx567888
/// @version 0.0.1
public final class FromUploadParameterHandlerBuilder implements ParameterHandlerBuilder {

    @Override
    public ParameterHandler tryBuild(ParameterInfo parameter) {
        // todo 这里可能有问题
        var javaType = parameter.parameterType();
        Class<?> rawType = javaType.rawClass();
        if (javaType instanceof ArrayTypeInfo a) {
            rawType = a.componentType().rawClass();
        }
        if (javaType instanceof ClassInfo c) {
            if (Collection.class.isAssignableFrom(javaType.rawClass())) {
                var superType = c.findSuperType(Collection.class);
                TypeInfo e = superType.bindings().get("E");
                if (e != null) {
                    rawType = e.rawClass();
                }
            }
        }
        if (rawType != MultiPartPart.class) {
            return null;
        }
        return new FromUploadParameterHandler(parameter);
    }

}
