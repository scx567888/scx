package cool.scx.ffm.mapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

/// 类型的映射 允许外部函数修改其值
/// 如果仅用来读取 请考虑直接使用基本类型
/// 因为目前 java 的基本类型无法用于泛型 所以 fromMemorySegment 返回 Object
///
/// @author scx567888
/// @version 0.0.1
public interface Mapper {

    /// 将内部数据转换为 MemorySegment (内存段)
    ///
    /// @param arena 作用域
    /// @return MemorySegment
    MemorySegment toMemorySegment(Arena arena);

    /// 从 MemorySegment (内存段) 设置值
    ///
    /// @param memorySegment a
    void fromMemorySegment(MemorySegment memorySegment);

}
