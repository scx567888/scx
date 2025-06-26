package cool.scx.bytes.supplier;

import cool.scx.bytes.ByteChunk;
import cool.scx.bytes.exception.ByteSupplierException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

/// ByteChannelByteSupplier
///
/// @author scx567888
/// @version 0.0.1
public class ByteChannelByteSupplier implements ByteSupplier {

    private final ReadableByteChannel dataChannel;
    private final int bufferLength;
    private final boolean compress;

    public ByteChannelByteSupplier(ReadableByteChannel dataChannel) {
        this(dataChannel, 8192, false);
    }

    public ByteChannelByteSupplier(ReadableByteChannel dataChannel, boolean compress) {
        this(dataChannel, 8192, compress);
    }

    public ByteChannelByteSupplier(ReadableByteChannel dataChannel, int bufferLength) {
        this(dataChannel, bufferLength, false);
    }

    public ByteChannelByteSupplier(ReadableByteChannel dataChannel, int bufferLength, boolean compress) {
        this.dataChannel = dataChannel;
        this.bufferLength = bufferLength;
        this.compress = compress;
    }

    @Override
    public ByteChunk get() throws ByteSupplierException {
        try {
            // 不使用成员变量作为缓冲区的原因 参照 InputStreamByteSupplier
            var bytes = ByteBuffer.allocate(bufferLength);
            int i = dataChannel.read(bytes);
            if (i == -1) {
                return null; // end of data
            }
            // 如果读取的数据量与缓冲区大小一致, 直接返回内部数组
            if (i == bufferLength) {
                return new ByteChunk(bytes.array());
            } else if (compress) {// 否则判断是否开启压缩
                var data = new byte[i];
                System.arraycopy(bytes.array(), 0, data, 0, i);
                return new ByteChunk(data);
            } else {// 不压缩 直接返回
                return new ByteChunk(bytes.array(), 0, i);
            }
        } catch (IOException e) {
            throw new ByteSupplierException(e);
        }
    }

}
