package cool.scx.io;

import cool.scx.io.LinkedDataReader.Node;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.util.function.Supplier;

public class ByteChannelDataSupplier implements Supplier<Node> {

    private final ByteChannel dataChannel;
    private final int bufferLength;
    private final boolean compress;

    public ByteChannelDataSupplier(ByteChannel dataChannel) {
        this(dataChannel, 8192, false);
    }

    public ByteChannelDataSupplier(ByteChannel dataChannel, boolean compress) {
        this(dataChannel, 8192, compress);
    }

    public ByteChannelDataSupplier(ByteChannel dataChannel, int bufferLength) {
        this(dataChannel, bufferLength, false);
    }

    public ByteChannelDataSupplier(ByteChannel dataChannel, int bufferLength, boolean compress) {
        this.dataChannel = dataChannel;
        this.bufferLength = bufferLength;
        this.compress = compress;
    }

    @Override
    public Node get() {
        try {
            //不使用成员变量作为缓冲区的原因 参照 InputStreamDataSupplier
            var bytes = ByteBuffer.allocate(bufferLength);
            int i = dataChannel.read(bytes);
            if (i == -1) {
                return null; // end of data
            }
            // 如果读取的数据量与缓冲区大小一致，直接返回内部数组
            if (i == bufferLength) {
                return new Node(bytes.array());
            } else if (compress) {// 否则判断是否开启压缩
                var data = new byte[i];
                System.arraycopy(bytes.array(), 0, data, 0, i);
                return new Node(data);
            } else {// 不压缩 直接返回
                return new Node(bytes.array(), 0, i);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
