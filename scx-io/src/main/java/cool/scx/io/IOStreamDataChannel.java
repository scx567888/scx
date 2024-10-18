package cool.scx.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Path;

public class IOStreamDataChannel implements DataChannel {

    private final InputStream in;
    private final OutputStream out;

    public IOStreamDataChannel(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void write(ByteBuffer buffer) throws IOException {
        var bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        out.write(bytes);
    }

    @Override
    public void write(byte[] bytes, int offset, int length) throws IOException {
        out.write(bytes, offset, length);
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        out.write(bytes);
    }

    @Override
    public void write(Path path, long offset, long length) throws IOException {

    }

    @Override
    public void write(Path path) throws IOException {

    }

    @Override
    public int read(ByteBuffer buffer) throws IOException {
        return 0;
    }

    @Override
    public int read(byte[] bytes, int offset, int length) throws IOException {
        return in.read(bytes, offset, length);
    }

    @Override
    public int read(byte[] bytes) throws IOException {
        return in.read(bytes);
    }

    @Override
    public void read(Path path, long offset, long length) throws IOException {

    }

    @Override
    public void read(Path path) throws IOException {

    }

    @Override
    public byte[] read(int maxLength) throws IOException, NoMoreDataException {
        byte[] buffer = new byte[maxLength];
        int bytesRead = in.read(buffer);
        if (bytesRead == -1) {
            throw new NoMoreDataException();
        }
        if (bytesRead == maxLength) {
            return buffer;
        } else {
            byte[] bytes = new byte[bytesRead];
            System.arraycopy(buffer, 0, bytes, 0, bytesRead);
            return bytes;
        }
    }

    @Override
    public void close() throws IOException {
        in.close();
        out.close();
    }

    @Override
    public boolean isOpen() throws IOException {
        return false;
    }

}
