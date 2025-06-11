package cool.scx.byte_reader.indexer;

import cool.scx.byte_reader.ByteChunk;

/// ByteIndexer
///
/// @author scx567888
/// @version 0.0.1
public interface ByteIndexer {

    /// 注意此方法支持回溯匹配 所以返回值可能出现负数
    /// 为了性能考虑 不使用异常来中断 当真正 未找到时 返回 Integer.MIN_VALUE
    /// 此方法会循环调用 所以注意内部处理
    /// 建议每次都重新创建
    ///
    /// @param chunk chunk
    /// @return l 索引值 (可能为负数)
    int indexOf(ByteChunk chunk);

}
