package cool.scx.config.handler.impl;

import cool.scx.ScxHandlerR;
import cool.scx.config.handler.ScxConfigHandlerParam;
import cool.scx.util.ansi.Ansi;

/**
 * a
 *
 * @param <T> a
 */
public record DefaultValueHandler<T>(T defaultVal) implements ScxHandlerR<ScxConfigHandlerParam, T> {

    @SuppressWarnings("unchecked")
    @Override
    public T handle(ScxConfigHandlerParam param) {
        T value = defaultVal != null ? new ConvertValueHandler<>((Class<T>) defaultVal.getClass()).handle(param) : (T) param.value();
        if (value != null) {
            return value;
        } else {
            Ansi.out().red("N 未检测到 " + param.ketPath() + " , 已采用默认值 : " + defaultVal).println();
            return defaultVal;
        }
    }

}