package cool.scx.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/**
 * 使用缓冲区的 InputStreamDataSupplier ,和 {@link InputStreamDataSupplier} 的性能相反
 * 1, 当大部分时候读取的数据长度等于 bufferLength 的时候, 性能会差一点, 因为会复制一遍数组
 * 2, 当大部分时候读取的数据长度小于 bufferLength 的时候, 性能会高一点,
 * 因为我们只创建了一个小于 bufferLength 长度数组
 * 但是当持有大量 BufferInputStreamDataSupplier 实例的时候内存占用可能高于 InputStreamDataSupplier
 *
 * @author scx567888
 * @version 0.0.1
 */
public class BufferInputStreamDataSupplier implements DataSupplier {

    private final InputStream inputStream;
    private final byte[] buffer;

    public BufferInputStreamDataSupplier(InputStream inputStream, int bufferLength) {
        this.inputStream = inputStream;
        this.buffer = new byte[bufferLength];
    }

    public BufferInputStreamDataSupplier(InputStream inputStream) {
        this(inputStream, 8192);
    }

    @Override
    public DataNode get() {
        try {
            int i = inputStream.read(buffer);
            if (i == -1) {
                return null; // 数据结束
            }
            var data = new byte[i];
            System.arraycopy(buffer, 0, data, 0, i); // 复制数据到新的数组
            return new DataNode(data);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
