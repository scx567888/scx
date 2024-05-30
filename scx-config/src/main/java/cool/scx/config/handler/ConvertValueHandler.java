package cool.scx.config.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import cool.scx.common.util.ObjectUtils;
import cool.scx.config.ScxConfigValueHandler;

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
    public T handle(String keyPath, Object rawValue) {
        if (rawValue != null) {
            return ObjectUtils.convertValue(rawValue, this.javaType, new ObjectUtils.Options().setIgnoreJsonIgnore(true));
        }
        return null;
    }

}
