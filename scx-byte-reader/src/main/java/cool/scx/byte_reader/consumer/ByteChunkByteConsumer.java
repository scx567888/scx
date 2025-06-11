package cool.scx.byte_reader.consumer;

import cool.scx.byte_reader.ByteChunk;

/// 接受为单个 DataNode
///
/// @author scx567888
/// @version 0.0.1
public class ByteChunkByteConsumer implements ByteConsumer {

    private ByteChunk byteChunk;

    @Override
    public boolean accept(ByteChunk byteChunk) {
        this.byteChunk = byteChunk;
        //只接受一次
        return false;
    }

    public ByteChunk getByteChunk() {
        return byteChunk;
    }

}
