package cool.scx.config.handler;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.ansi.Ansi;
import cool.scx.common.util.ObjectUtils;
import cool.scx.config.ScxConfigValueHandler;

/// DefaultValueHandler
///
/// @author scx567888
/// @version 0.0.1
public final class DefaultValueHandler<T> implements ScxConfigValueHandler<T> {

    private final T defaultVal;

    private DefaultValueHandler(T defaultVal) {
        this.defaultVal = defaultVal;
    }

    public static <H> DefaultValueHandler<H> of(H defaultVal) {
        return new DefaultValueHandler<>(defaultVal);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T handle(String keyPath, JsonNode rawValue) {
        Object value = this.defaultVal != null ? ObjectUtils.convertValue(rawValue, this.defaultVal.getClass()) : rawValue;
        if (value != null) {
            return (T) value;
        } else {
            Ansi.ansi().red("N 未检测到 " + keyPath + " , 已采用默认值 : " + this.defaultVal).println();
            return this.defaultVal;
        }
    }

}
