package cool.scx.common.ffm.type;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

/**
 * 引用类型需要继承此接口
 */
public interface Ref {

    MemorySegment init(Arena arena);

    void refresh();

}
