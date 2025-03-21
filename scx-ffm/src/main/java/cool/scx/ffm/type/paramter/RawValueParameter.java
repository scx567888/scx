package cool.scx.ffm.type.paramter;

import java.lang.foreign.Arena;

/// 原始值
///
/// @author scx567888
/// @version 0.0.1
public class RawValueParameter implements Parameter {

    private final Object value;

    public RawValueParameter(Object value) {
        this.value = value;
    }

    @Override
    public Object toNativeParameter(Arena arena) {
        return value;
    }

}
