package cool.scx.config.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.util.ObjectUtils;
import cool.scx.config.ScxConfigValueHandler;

/// ConvertValueHandler
///
/// @author scx567888
/// @version 0.0.1
public final class ConvertValueHandler<T> implements ScxConfigValueHandler<T> {

    private final JavaType javaType;

    private ConvertValueHandler(JavaType javaType) {
        this.javaType = javaType;
    }

    public static <H> ConvertValueHandler<H> of(Class<H> tClass) {
        return new ConvertValueHandler<>(ObjectUtils.constructType(tClass));
    }

    public static <H> ConvertValueHandler<H> of(TypeReference<H> tTypeReference) {
        return new ConvertValueHandler<>(ObjectUtils.constructType(tTypeReference));
    }

    @Override
    public T handle(String keyPath, JsonNode rawValue) {
        if (rawValue != null) {
            return ObjectUtils.convertValue(rawValue, this.javaType, new ObjectUtils.Options().setIgnoreJsonIgnore(true));
        }
        return null;
    }

}
