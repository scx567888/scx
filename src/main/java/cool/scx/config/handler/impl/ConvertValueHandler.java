package cool.scx.config.handler.impl;

import cool.scx.ScxHandlerR;
import cool.scx.config.handler.ScxConfigHandlerParam;
import cool.scx.util.ObjectUtils;

public record ConvertValueHandler<T>(Class<T> tClass) implements ScxHandlerR<ScxConfigHandlerParam, T> {

    @Override
    public T handle(ScxConfigHandlerParam param) {
        return param.value() != null ? ObjectUtils.convertValue(param.value(), tClass) : null;
    }

}