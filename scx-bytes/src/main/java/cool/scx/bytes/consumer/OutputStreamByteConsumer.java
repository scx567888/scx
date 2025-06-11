package cool.scx.bytes.consumer;

import cool.scx.bytes.ByteChunk;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;

/// 写入到输出流中
///
/// @author scx567888
/// @version 0.0.1
public class OutputStreamByteConsumer implements ByteConsumer {

    private final OutputStream out;

    //记录总的长度 有时候会用到
    private long byteCount;

    public OutputStreamByteConsumer(OutputStream out) {
        this.out = out;
        this.byteCount = 0;
    }

    @Override
    public boolean accept(ByteChunk chunk) {
        try {
            out.write(chunk.bytes, chunk.startPosition, chunk.length);
            byteCount += chunk.length;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return true;
    }

    public long byteCount() {
        return byteCount;
    }

}
