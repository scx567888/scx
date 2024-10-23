package cool.scx.io;

import cool.scx.io.LinkedDataReader.Node;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.function.Supplier;

public class InputStreamDataSupplier implements Supplier<Node> {

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
    public Node get() {
        try {
            var bytes = new byte[bufferLength];
            int i = inputStream.read(bytes);
            if (i == -1) {
                return null; // end of data
            }
            // 如果读取的数据量与缓冲区大小一致，直接返回内部数组
            if (i == bufferLength) {
                return new Node(bytes);
            } else if (compress) {// 否则判断是否开启压缩
                var data = new byte[i];
                System.arraycopy(bytes, 0, data, 0, i);
                return new Node(data);
            } else {// 不压缩 直接返回
                return new Node(bytes, 0, i);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
