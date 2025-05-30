package cool.scx.io.io_stream;

import java.io.IOException;
import java.io.OutputStream;

/// 支持 检查是否关闭的 输出流
///
/// @author scx567888
/// @version 0.0.1
public abstract class CheckedOutputStream extends OutputStream {

    protected volatile boolean closed;

    protected void ensureOpen() throws IOException {
        if (closed) {
            throw new StreamClosedException();
        }
    }

    public boolean isClosed() {
        return closed;
    }

}
