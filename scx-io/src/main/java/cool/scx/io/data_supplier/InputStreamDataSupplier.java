package cool.scx.io.data_supplier;

import cool.scx.io.data_node.DataNode;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/**
 * InputStreamDataSupplier
 *
 * @author scx567888
 * @version 0.0.1
 */
public class InputStreamDataSupplier implements DataSupplier {

    private final InputStream inputStream;
    private final int bufferLength;
    private final boolean compress;

    public InputStreamDataSupplier(InputStream inputStream) {
        this(inputStream, 8192, false);
    }

    public InputStreamDataSupplier(InputStream inputStream, boolean compress) {
        this(inputStream, 8192, compress);
    }

    public InputStreamDataSupplier(InputStream inputStream, int bufferLength) {
        this(inputStream, bufferLength, false);
    }

    public InputStreamDataSupplier(InputStream inputStream, int bufferLength, boolean compress) {
        this.inputStream = inputStream;
        this.bufferLength = bufferLength;
        this.compress = compress;
    }

    @Override
    public DataNode get() {
        try {
            // 这里每次都创建一个 byte 数组是因为我们后续需要直接使用 这个数组
            // 即使使用成员变量 来作为缓冲 buffer
            // 也是需要重新分配 一个新的数组 来将数据复制过去 所以本质上并没有区别
            // 甚至这种情况再同时持有多个 InputStreamDataSupplier 的时候 内存占用会更少 因为没有成员变量
            var bytes = new byte[bufferLength];
            int i = inputStream.read(bytes);
            if (i == -1) {
                return null; // end of data
            }
            // 如果读取的数据量与缓冲区大小一致，直接返回内部数组
            if (i == bufferLength) {
                return new DataNode(bytes);
            } else if (compress) {// 否则判断是否开启压缩
                var data = new byte[i];
                System.arraycopy(bytes, 0, data, 0, i);
                return new DataNode(data);
            } else {// 不压缩 直接返回
                return new DataNode(bytes, 0, i);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
