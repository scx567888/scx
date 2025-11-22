package cool.scx.config.handler;

import cool.scx.config.ScxConfigValueHandler;
import cool.scx.object.ScxObject;
import cool.scx.object.node.Node;
import dev.scx.reflect.ScxReflect;
import dev.scx.reflect.TypeInfo;
import dev.scx.reflect.TypeReference;

/// ConvertValueHandler
///
/// @author scx567888
/// @version 0.0.1
public final class ConvertValueHandler<T> implements ScxConfigValueHandler<T> {

    private final TypeInfo javaType;

    private ConvertValueHandler(TypeInfo javaType) {
        this.javaType = javaType;
    }

    public static <H> ConvertValueHandler<H> of(Class<H> tClass) {
        return new ConvertValueHandler<>(ScxReflect.typeOf(tClass));
    }

    public static <H> ConvertValueHandler<H> of(TypeReference<H> tTypeReference) {
        return new ConvertValueHandler<>(ScxReflect.typeOf(tTypeReference));
    }

    @Override
    public T handle(String keyPath, Node rawValue) {
        if (rawValue != null) {
            return ScxObject.nodeToValue(rawValue, this.javaType);
        }
        return null;
    }

}
