package cool.scx.bytes.supplier;

import cool.scx.bytes.ByteChunk;
import cool.scx.bytes.exception.ByteSupplierException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

/// BufferedByteChannelByteSupplier
///
/// @author scx567888
/// @version 0.0.1
public class BufferedByteChannelByteSupplier implements ByteSupplier {

    private final ReadableByteChannel dataChannel;
    private final ByteBuffer buffer;

    public BufferedByteChannelByteSupplier(ReadableByteChannel dataChannel, int bufferLength) {
        this.dataChannel = dataChannel;
        this.buffer = ByteBuffer.allocate(bufferLength);
    }

    public BufferedByteChannelByteSupplier(ReadableByteChannel dataChannel) {
        this(dataChannel, 8192);
    }

    @Override
    public ByteChunk get() throws ByteSupplierException {
        try {
            buffer.clear(); // 重置缓冲区以进行新的读取操作
            int i = dataChannel.read(buffer);
            if (i == -1) {
                return null; // 数据结束
            }
            var data = new byte[i];
            buffer.flip().get(data); // 复制数据到新的数组
            return new ByteChunk(data);
        } catch (IOException e) {
            throw new ByteSupplierException(e);
        }
    }

}
