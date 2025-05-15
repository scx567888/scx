package cool.scx.io.data_consumer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;

/// 写入到输出流中
/// @author scx567888
/// @version 0.0.1
public class OutputStreamDataConsumer implements DataConsumer {

    private final OutputStream out;

    //记录总的长度 有时候会用到
    private long byteCount;

    public OutputStreamDataConsumer(OutputStream out) {
        this.out = out;
        this.byteCount = 0;
    }

    @Override
    public boolean accept(byte[] bytes, int position, int length) {
        try {
            out.write(bytes, position, length);
            byteCount += length;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return true;
    }

    public long byteCount() {
        return byteCount;
    }

}
