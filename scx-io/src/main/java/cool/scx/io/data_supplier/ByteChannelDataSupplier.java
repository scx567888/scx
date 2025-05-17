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
public class ByteChannelDataSupplier implements DataSupplier {

    private final ReadableByteChannel dataChannel;
    private final int bufferLength;
    private final boolean compress;

    public ByteChannelDataSupplier(ReadableByteChannel dataChannel) {
        this(dataChannel, 8192, false);
    }

    public ByteChannelDataSupplier(ReadableByteChannel dataChannel, boolean compress) {
        this(dataChannel, 8192, compress);
    }

    public ByteChannelDataSupplier(ReadableByteChannel dataChannel, int bufferLength) {
        this(dataChannel, bufferLength, false);
    }

    public ByteChannelDataSupplier(ReadableByteChannel dataChannel, int bufferLength, boolean compress) {
        this.dataChannel = dataChannel;
        this.bufferLength = bufferLength;
        this.compress = compress;
    }

    @Override
    public DataNode get() throws DataSupplierException {
        try {
            // 不使用成员变量作为缓冲区的原因 参照 InputStreamDataSupplier
            var bytes = ByteBuffer.allocate(bufferLength);
            int i = dataChannel.read(bytes);
            if (i == -1) {
                return null; // end of data
            }
            // 如果读取的数据量与缓冲区大小一致，直接返回内部数组
            if (i == bufferLength) {
                return new DataNode(bytes.array());
            } else if (compress) {// 否则判断是否开启压缩
                var data = new byte[i];
                System.arraycopy(bytes.array(), 0, data, 0, i);
                return new DataNode(data);
            } else {// 不压缩 直接返回
                return new DataNode(bytes.array(), 0, i);
            }
        } catch (IOException e) {
            throw new DataSupplierException(e);
        }
    }

}
