package cool.scx.io.io_stream;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/// NullCheckedInputStream
///
/// @author scx567888
/// @version 0.0.1
public class NullCheckedInputStream extends CheckedInputStream {

    @Override
    public int available() throws IOException {
        ensureOpen();
        return 0;
    }

    @Override
    public int read() throws IOException {
        ensureOpen();
        return -1;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        Objects.checkFromIndexSize(off, len, b.length);
        if (len == 0) {
            return 0;
        }
        ensureOpen();
        return -1;
    }

    @Override
    public byte[] readAllBytes() throws IOException {
        ensureOpen();
        return new byte[0];
    }

    @Override
    public int readNBytes(byte[] b, int off, int len)
            throws IOException {
        Objects.checkFromIndexSize(off, len, b.length);
        ensureOpen();
        return 0;
    }

    @Override
    public byte[] readNBytes(int len) throws IOException {
        if (len < 0) {
            throw new IllegalArgumentException("len < 0");
        }
        ensureOpen();
        return new byte[0];
    }

    @Override
    public long skip(long n) throws IOException {
        ensureOpen();
        return 0L;
    }

    @Override
    public void skipNBytes(long n) throws IOException {
        ensureOpen();
        if (n > 0) {
            throw new EOFException();
        }
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        Objects.requireNonNull(out);
        ensureOpen();
        return 0L;
    }

    @Override
    public void close() throws IOException {
        closed = true;
    }

}
