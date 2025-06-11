package cool.scx.byte_reader.supplier;

import cool.scx.byte_reader.ByteNode;
import cool.scx.byte_reader.exception.ByteSupplierException;

import java.io.IOException;
import java.io.InputStream;

/// BufferedInputStreamDataSupplier
/// 和 [InputStreamByteSupplier] 性能相反
/// 1, 当大部分时候读取的数据长度等于 bufferLength 的时候, 性能会差一点 因为多了一次复制
/// 2, 当大部分时候读取的数据长度小于 bufferLength 的时候, 性能会好一点 因为只会创建一个较小的数组并复制数据
///
/// @author scx567888
/// @version 0.0.1
public class BufferedInputStreamByteSupplier implements ByteSupplier {

    private final InputStream inputStream;
    private final byte[] buffer;

    public BufferedInputStreamByteSupplier(InputStream inputStream, int bufferLength) {
        this.inputStream = inputStream;
        this.buffer = new byte[bufferLength];
    }

    public BufferedInputStreamByteSupplier(InputStream inputStream) {
        this(inputStream, 8192);
    }

    @Override
    public ByteNode get() throws ByteSupplierException {
        try {
            int i = inputStream.read(buffer);
            if (i == -1) {
                return null; // 数据结束
            }
            var data = new byte[i];
            System.arraycopy(buffer, 0, data, 0, i); // 复制数据到新的数组
            return new ByteNode(data);
        } catch (IOException e) {
            throw new ByteSupplierException(e);
        }
    }

}
