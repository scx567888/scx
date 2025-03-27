package cool.scx.io.io_stream;

import cool.scx.io.data_reader.PowerfulLinkedDataReader;

import java.io.IOException;
import java.io.OutputStream;

/// 固定长度的 读取器
///
/// @author scx567888
/// @version 0.0.1
public class FixedLengthDataReaderInputStream extends CheckedInputStream {

    private final PowerfulLinkedDataReader dataReader;
    private final long maxLength;
    private long position;
    private volatile boolean closed;

    public FixedLengthDataReaderInputStream(PowerfulLinkedDataReader dataReader, long maxLength) {
        this.dataReader = dataReader;
        this.maxLength = maxLength;
        this.position = 0;
    }

    @Override
    public int read() throws IOException {
        ensureOpen();
        if (position >= maxLength) {
            return -1;
        }
        int i = dataReader.inputStreamRead();
        if (i == -1) {
            position = maxLength;
            return -1;
        } else {
            position = position + 1;
            return i;
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        ensureOpen();
        if (position >= maxLength) {
            return -1;
        }
        var length = Math.min(len, maxLength - position);
        var i = dataReader.inputStreamRead(b, off, (int) length);
        if (i == -1) {
            position = maxLength;
            return -1;
        } else {
            position = position + i;
            return i;
        }
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        ensureOpen();
        if (position >= maxLength) {
            return -1;
        }
        var length = maxLength - position;
        var i = dataReader.inputStreamTransferTo(out, length);
        if (i == -1) {
            position = maxLength;
            return -1;
        } else {
            position = position + i;
            return i;
        }
    }

    @Override
    public void close() throws IOException {
        closed = true;
    }

}
