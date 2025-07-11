package cool.scx.config.handler;

import cool.scx.ansi.Ansi;
import cool.scx.config.ScxConfigValueHandler;
import cool.scx.object.ScxObject;
import cool.scx.object.node.Node;

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
    public T handle(String keyPath, Node rawValue) {
        Object value = this.defaultVal != null ? ScxObject.convertValue(rawValue, this.defaultVal.getClass()) : rawValue;
        if (value != null) {
            return (T) value;
        } else {
            Ansi.ansi().red("N 未检测到 " + keyPath + " , 已采用默认值 : " + this.defaultVal).println();
            return this.defaultVal;
        }
    }

}
