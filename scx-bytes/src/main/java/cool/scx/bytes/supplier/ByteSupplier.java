package cool.scx.bytes.supplier;

import cool.scx.bytes.ByteChunk;
import cool.scx.bytes.exception.ByteSupplierException;

import java.io.IOException;

/// ByteSupplier
///
/// @author scx567888
/// @version 0.0.1
public interface ByteSupplier extends AutoCloseable {

    /// 如果没有数据请返回 null
    ByteChunk get() throws ByteSupplierException;

    @Override
    default void close() throws IOException {

    }

}
