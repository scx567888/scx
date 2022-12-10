package cool.scx.config.handler_impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import cool.scx.config.ScxConfigValueHandler;
import cool.scx.util.ObjectUtils;

/**
 * a
 *
 * @param <T> a
 * @author scx567888
 * @version 0.0.1
 */
public final class ConvertValueHandler<T> implements ScxConfigValueHandler<T> {

    private final JavaType javaType;

    /**
     * <p>Constructor for ConvertValueHandler.</p>
     *
     * @param javaType a {@link com.fasterxml.jackson.databind.JavaType} object
     */
    private ConvertValueHandler(JavaType javaType) {
        this.javaType = javaType;
    }

    /**
     * <p>of.</p>
     *
     * @param tClass a {@link java.lang.Class} object
     * @param <H>    a H class
     * @return a {@link cool.scx.config.handler_impl.ConvertValueHandler} object
     */
    public static <H> ConvertValueHandler<H> of(Class<H> tClass) {
        return new ConvertValueHandler<>(ObjectUtils.constructType(tClass));
    }

    /**
     * <p>of.</p>
     *
     * @param tTypeReference a {@link com.fasterxml.jackson.core.type.TypeReference} object
     * @param <H>            a H class
     * @return a {@link cool.scx.config.handler_impl.ConvertValueHandler} object
     */
    public static <H> ConvertValueHandler<H> of(TypeReference<H> tTypeReference) {
        return new ConvertValueHandler<>(ObjectUtils.constructType(tTypeReference));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T handle(String keyPath, Object rawValue) {
        if (rawValue != null) {
            return ObjectUtils.convertValue(rawValue, this.javaType, ObjectUtils.Option.IGNORE_JSON_IGNORE);
        }
        return null;
    }

}
