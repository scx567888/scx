package cool.scx.io.io_stream;

import java.io.IOException;

/// 什么都不处理的 输出流
///
/// @author scx567888
/// @version 0.0.1
public class NullCheckedOutputStream extends CheckedOutputStream {

    @Override
    public void write(int b) throws IOException {
        ensureOpen();
    }

    @Override
    public void write(byte[] b) throws IOException {
        ensureOpen();
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        ensureOpen();
    }

    @Override
    public void flush() throws IOException {
        ensureOpen();
    }

    @Override
    public void close() throws IOException {
        closed = true;
    }

}
