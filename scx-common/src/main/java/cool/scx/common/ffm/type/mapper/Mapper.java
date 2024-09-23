package cool.scx.common.ffm.type.mapper;

import cool.scx.common.ffm.type.wrapper.Wrapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

/**
 * 类型的映射 允许外部函数修改其值
 * 如果仅用来读取 请考虑使用 {@link Wrapper } 或直接使用基本类型
 */
public interface Mapper {

    /**
     * 将内部数据转换为 MemorySegment (内存段)
     *
     * @param arena 作用域
     * @return MemorySegment
     */
    MemorySegment toMemorySegment(Arena arena);

    /**
     * 从 MemorySegment (内存段) 设置值
     *
     * @param memorySegment a
     */
    void fromMemorySegment(MemorySegment memorySegment);

}
