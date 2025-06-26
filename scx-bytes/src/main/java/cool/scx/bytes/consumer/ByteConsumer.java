package cool.scx.bytes.consumer;

import cool.scx.bytes.ByteChunk;

/// ByteConsumer
///
/// @author scx567888
/// @version 0.0.1
public interface ByteConsumer<E extends Throwable> {

    /// @return 是否需要更多数据
    boolean accept(ByteChunk chunk) throws E;

}
