package cool.scx.io;

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
    private final byte[] buffer;

    public InputStreamDataSupplier(InputStream inputStream, int bufferLength) {
        this.inputStream = inputStream;
        this.buffer = new byte[bufferLength];
    }

    public InputStreamDataSupplier(InputStream inputStream) {
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
