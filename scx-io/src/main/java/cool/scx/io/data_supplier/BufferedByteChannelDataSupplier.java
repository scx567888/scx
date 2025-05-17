package cool.scx.io.data_supplier;

import cool.scx.io.data_node.DataNode;
import cool.scx.io.exception.DataSupplierException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

/// ByteChannelDataSupplier
///
/// @author scx567888
/// @version 0.0.1
public class BufferedByteChannelDataSupplier implements DataSupplier {

    private final ReadableByteChannel dataChannel;
    private final ByteBuffer buffer;

    public BufferedByteChannelDataSupplier(ReadableByteChannel dataChannel, int bufferLength) {
        this.dataChannel = dataChannel;
        this.buffer = ByteBuffer.allocate(bufferLength);
    }

    public BufferedByteChannelDataSupplier(ReadableByteChannel dataChannel) {
        this(dataChannel, 8192);
    }

    @Override
    public DataNode get() throws DataSupplierException {
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
            throw new DataSupplierException(e);
        }
    }

}
