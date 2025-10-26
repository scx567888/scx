package cool.scx.io.x.io_stream;

import cool.scx.bytes.ByteReader;
import cool.scx.bytes.supplier.ByteSupplier;

import java.io.IOException;
import java.io.OutputStream;

/// 包装 ByteReader 的输入流
///
/// @author scx567888
/// @version 0.0.1
public class ByteReaderInputStream extends CheckedInputStream {

    private final ByteReader dataReader;

    public ByteReaderInputStream(ByteReader dataReader) {
        this.dataReader = dataReader;
    }

    public ByteReaderInputStream(ByteSupplier dataSupplier) {
        this.dataReader = new ByteReader(dataSupplier);
    }

    @Override
    public int read() throws IOException {
        ensureOpen();
        return dataReader.inputStreamRead();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        ensureOpen();
        return dataReader.inputStreamRead(b, off, len);
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        ensureOpen();
        return dataReader.inputStreamTransferTo(out);
    }

    public ByteReader dataReader() {
        return dataReader;
    }

    @Override
    public void mark(int readlimit) {
        dataReader.mark();
    }

    @Override
    public void reset() throws IOException {
        dataReader.reset();
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public byte[] readNBytes(int len) throws IOException {
        return dataReader.inputStreamReadNBytes(len);
    }

    @Override
    public byte[] readAllBytes() throws IOException {
        return dataReader.inputStreamReadNBytes(Integer.MAX_VALUE);
    }

    @Override
    public void close() throws IOException {
        closed = true;
    }

}
