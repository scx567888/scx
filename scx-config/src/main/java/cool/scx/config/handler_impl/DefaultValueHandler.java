package cool.scx.config.handler_impl;

import cool.scx.config.ScxConfigValueHandler;
import cool.scx.util.ObjectUtils;
import cool.scx.util.ansi.Ansi;

/**
 * a
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class DefaultValueHandler<T> implements ScxConfigValueHandler<T> {

    private final T defaultVal;

    /**
     * <p>Constructor for DefaultValueHandler.</p>
     *
     * @param defaultVal a T object
     */
    private DefaultValueHandler(T defaultVal) {
        this.defaultVal = defaultVal;
    }

    /**
     * <p>of.</p>
     *
     * @param defaultVal a H object
     * @param <H>        a H class
     * @return a {@link cool.scx.config.handler_impl.DefaultValueHandler} object
     */
    public static <H> DefaultValueHandler<H> of(H defaultVal) {
        return new DefaultValueHandler<>(defaultVal);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public T handle(String keyPath, Object rawValue) {
        Object value = this.defaultVal != null ? ObjectUtils.convertValue(rawValue, this.defaultVal.getClass()) : rawValue;
        if (value != null) {
            return (T) value;
        } else {
            Ansi.out().red("N 未检测到 " + keyPath + " , 已采用默认值 : " + this.defaultVal).println();
            return this.defaultVal;
        }
    }

}
