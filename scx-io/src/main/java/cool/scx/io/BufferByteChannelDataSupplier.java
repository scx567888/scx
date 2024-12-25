package cool.scx.io;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

/**
 * 参见 {@link BufferInputStreamDataSupplier}
 */
public class BufferByteChannelDataSupplier implements DataSupplier {

    private final ReadableByteChannel dataChannel;
    private final ByteBuffer buffer;

    public BufferByteChannelDataSupplier(ReadableByteChannel dataChannel, int bufferLength) {
        this.dataChannel = dataChannel;
        this.buffer = ByteBuffer.allocate(bufferLength);
    }

    public BufferByteChannelDataSupplier(ReadableByteChannel dataChannel) {
        this(dataChannel, 8192);
    }

    @Override
    public DataNode get() {
        try {
            buffer.clear(); // 重置缓冲区以进行新的读取操作
            int i = dataChannel.read(buffer);
            if (i == -1) {
                return null; // 数据结束
            }
            var data = new byte[i];
            buffer.flip().get(data); // 复制数据到新的数组
            return new DataNode(data);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
