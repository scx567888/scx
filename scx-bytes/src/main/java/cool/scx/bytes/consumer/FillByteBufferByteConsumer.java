package cool.scx.bytes.consumer;

import cool.scx.bytes.ByteChunk;

import java.nio.ByteBuffer;

/// FillByteBufferByteConsumer
///
/// @author scx567888
/// @version 0.0.1
public class FillByteBufferByteConsumer implements ByteConsumer<RuntimeException> {

    private final ByteBuffer data;
    private final int startPosition;

    public FillByteBufferByteConsumer(ByteBuffer data) {
        this.data = data;
        this.startPosition = data.position();
    }

    @Override
    public boolean accept(ByteChunk byteChunk) {
        data.put(byteChunk.bytes, byteChunk.start, byteChunk.length);
        return true;
    }

    public int filledLength() {
        return data.position() - startPosition;
    }

}
