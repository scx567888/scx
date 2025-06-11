package cool.scx.byte_reader.supplier;

import cool.scx.byte_reader.ByteChunk;
import cool.scx.byte_reader.exception.ByteSupplierException;

/// 数据生产者
///
/// @author scx567888
/// @version 0.0.1
public interface ByteSupplier {

    /// 获取数据 如果没有数据请返回 null
    ///
    /// @return 数据节点 或 null
    ByteChunk get() throws ByteSupplierException;

}
