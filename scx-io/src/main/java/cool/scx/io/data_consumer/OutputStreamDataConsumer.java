package cool.scx.io.data_consumer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;

public class OutputStreamDataConsumer implements DataConsumer {

    private final OutputStream out;

    //记录总的长度 有时候会用到
    private long byteCount;

    public OutputStreamDataConsumer(OutputStream out) {
        this.out = out;
        this.byteCount = 0;
    }

    @Override
    public void accept(byte[] bytes, int position, int length) {
        try {
            out.write(bytes, position, length);
            byteCount += length;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public long byteCount() {
        return byteCount;
    }

}
