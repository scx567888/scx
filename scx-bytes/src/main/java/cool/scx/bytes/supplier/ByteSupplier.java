package cool.scx.bytes.supplier;

import cool.scx.bytes.ByteChunk;
import cool.scx.bytes.exception.ByteSupplierException;

/// ByteSupplier
///
/// @author scx567888
/// @version 0.0.1
public interface ByteSupplier {

    /// 如果没有数据请返回 null
    ByteChunk get() throws ByteSupplierException;

}
