package cool.scx.io.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 懒加载输入流
 *
 * @author scx567888
 * @version 0.0.1
 */
public abstract class LazyInputStream extends InputStream {

    private InputStream inputStream;

    public InputStream toInputStream() {
        if (inputStream == null) {
            inputStream = toInputStream0();
        }
        return inputStream;
    }

    protected abstract InputStream toInputStream0();

    @Override
    public int read() throws IOException {
        return toInputStream().read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return toInputStream().read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return toInputStream().read(b, off, len);
    }

    @Override
    public byte[] readAllBytes() throws IOException {
        return toInputStream().readAllBytes();
    }

    @Override
    public byte[] readNBytes(int len) throws IOException {
        return toInputStream().readNBytes(len);
    }

    @Override
    public int readNBytes(byte[] b, int off, int len) throws IOException {
        return toInputStream().readNBytes(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return toInputStream().skip(n);
    }

    @Override
    public void skipNBytes(long n) throws IOException {
        toInputStream().skipNBytes(n);
    }

    @Override
    public int available() throws IOException {
        return toInputStream().available();
    }

    @Override
    public void close() throws IOException {
        toInputStream().close();
    }

    @Override
    public void mark(int readlimit) {
        toInputStream().mark(readlimit);
    }

    @Override
    public void reset() throws IOException {
        toInputStream().reset();
    }

    @Override
    public boolean markSupported() {
        return toInputStream().markSupported();
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        return toInputStream().transferTo(out);
    }

}
