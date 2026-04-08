package cool.scx.bytes.consumer;

import cool.scx.bytes.ByteChunk;

/// SkipByteConsumer
///
/// @author scx567888
/// @version 0.0.1
public final class SkipByteConsumer implements ByteConsumer<RuntimeException> {

    public static final SkipByteConsumer SKIP_BYTE_CONSUMER = new SkipByteConsumer();

    @Override
    public boolean accept(ByteChunk chunk) {
        return true;
    }

}
