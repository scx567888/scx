package cool.scx.bytes.io;

import cool.scx.bytes.IByteReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ByteReaderInputStream extends InputStream implements ByteReaderWrapper {

    private final IByteReader byteReader;

    public ByteReaderInputStream(IByteReader byteReader) {
        this.byteReader = byteReader;
    }

    @Override
    public int read() throws IOException {
        return byteReader.inputStreamRead();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return byteReader.inputStreamRead(b, off, len);
    }

    @Override
    public byte[] readNBytes(int len) throws IOException {
        return byteReader.inputStreamReadNBytes(len);
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        return byteReader.inputStreamTransferTo(out);
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public void mark(int readlimit) {
        byteReader.mark();
    }

    @Override
    public void reset() {
        byteReader.reset();
    }

    @Override
    public void close() throws IOException {
        byteReader.close();
    }

    @Override
    public IByteReader byteReader() {
        return byteReader;
    }

}
