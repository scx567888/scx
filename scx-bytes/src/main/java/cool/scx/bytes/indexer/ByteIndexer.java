package cool.scx.bytes.indexer;

import cool.scx.bytes.ByteChunk;

/// ByteIndexer
///
/// @author scx567888
/// @version 0.0.1
public interface ByteIndexer {

    /// 支持跨 chunk 的回溯匹配, 因此返回值可能为负数
    /// 若未匹配到 请返回 Integer.MIN_VALUE
    ///
    /// @param chunk chunk
    /// @return 匹配的索引位置
    int indexOf(ByteChunk chunk);

}
