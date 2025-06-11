package cool.scx.byte_reader.consumer;

import cool.scx.byte_reader.ByteChunk;

/// DataConsumer
///
/// @author scx567888
/// @version 0.0.1
public interface ByteConsumer {

    /// @return 是否需要更多数据
    boolean accept(ByteChunk chunk);

}
