package cool.scx.common.ffm;

import java.lang.foreign.Arena;

/**
 * 原始值 REF 
 */
public class RawParameter implements Parameter{

    private final Object value;

    public RawParameter(Object value) {
        this.value=value;
    }

    @Override
    public Object toNativeParameter(Arena arena) {
        return value;
    }
    
}
