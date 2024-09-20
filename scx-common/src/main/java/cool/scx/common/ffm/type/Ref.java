package cool.scx.common.ffm.type;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

/**
 * 引用类型需要继承此接口
 */
public interface Ref {

    /**
     * 将数据加载到 MemorySegment (内存段)
     *
     * @param arena 作用域
     * @return MemorySegment
     */
    MemorySegment writeToMemorySegment(Arena arena);

    /**
     * 从内存段加载
     * 因为脱离 Arena 作用域之后 MemorySegment 将无法读取
     * 所以此处预先 将 数据 读取到 Ref 中
     */
    void readFromMemorySegment();

}
