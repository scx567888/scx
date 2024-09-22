package cool.scx.common.ffm;

import java.lang.foreign.Arena;

public interface Parameter {

   Object toNativeParameter(Arena arena);
    
}
