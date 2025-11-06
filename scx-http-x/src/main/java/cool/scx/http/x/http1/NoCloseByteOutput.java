package cool.scx.http.x.http1;

import cool.scx.io.ByteChunk;
import cool.scx.io.ByteOutput;
import cool.scx.io.exception.AlreadyClosedException;
import cool.scx.io.exception.ScxIOException;

/// NoCloseOutputStream
///
/// @author scx567888
/// @version 0.0.1
public class NoCloseByteOutput implements ByteOutput {

    private final ByteOutput out;

    private boolean closed;

    public NoCloseByteOutput(ByteOutput out) {
        this.out = out;
        this.closed = false;
    }

    private void ensureOpen() throws AlreadyClosedException {
        if (closed) {
            throw new AlreadyClosedException();
        }
    }

    @Override
    public void write(byte b) throws ScxIOException, AlreadyClosedException {
        ensureOpen();

        this.out.write(b);
    }

    @Override
    public void write(ByteChunk b) throws ScxIOException, AlreadyClosedException {
        ensureOpen();

        this.out.write(b);
    }

    @Override
    public void flush() throws ScxIOException, AlreadyClosedException {
        ensureOpen();

        this.out.flush();
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void close() throws ScxIOException, AlreadyClosedException {
        ensureOpen();

        // 这里中断 close, 改为刷新
        this.out.flush();
        closed = true; // 只有成功关闭才算作 关闭
    }

}
