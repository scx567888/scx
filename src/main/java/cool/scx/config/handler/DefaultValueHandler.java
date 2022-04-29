package cool.scx.config.handler;

import cool.scx.functional.ScxHandlerR;
import cool.scx.tuple.KeyValue;
import cool.scx.util.ansi.Ansi;

/**
 * a
 *
 * @param <T> a
 */
public record DefaultValueHandler<T>(T defaultVal) implements ScxHandlerR<KeyValue<String, Object>, T> {

    @SuppressWarnings("unchecked")
    @Override
    public T handle(KeyValue<String, Object> param) {
        T value = defaultVal != null ? new ConvertValueHandler<>((Class<T>) defaultVal.getClass()).handle(param) : (T) param.value();
        if (value != null) {
            return value;
        } else {
            Ansi.out().red("N 未检测到 " + param.key() + " , 已采用默认值 : " + defaultVal).println();
            return defaultVal;
        }
    }

}