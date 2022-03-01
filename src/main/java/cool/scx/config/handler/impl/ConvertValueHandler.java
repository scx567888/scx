package cool.scx.config.handler.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import cool.scx.ScxHandlerR;
import cool.scx.config.handler.ScxConfigHandlerParam;
import cool.scx.util.ObjectUtils;

import java.util.Objects;

/**
 * a
 *
 * @param <T> a
 * @author scx567888
 * @version 1.11.8
 */
public final class ConvertValueHandler<T> implements ScxHandlerR<ScxConfigHandlerParam, T> {

    private final JavaType javaType;

    /**
     * a
     *
     * @param tClass a
     */
    public ConvertValueHandler(Class<T> tClass) {
        Objects.requireNonNull(tClass, "Class 不能为空 !!!");
        this.javaType = ObjectUtils.constructType(tClass);
    }

    /**
     * a
     *
     * @param tTypeReference a
     */
    public ConvertValueHandler(TypeReference<T> tTypeReference) {
        Objects.requireNonNull(tTypeReference, "TypeReference 不能为空 !!!");
        this.javaType = ObjectUtils.constructType(tTypeReference);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T handle(ScxConfigHandlerParam param) {
        return param.value() != null ? ObjectUtils.convertValue(param.value(), this.javaType) : null;
    }

}
