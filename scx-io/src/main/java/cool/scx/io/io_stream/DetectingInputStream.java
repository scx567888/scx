package cool.scx.io.io_stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;

public class DetectingInputStream extends InputStream {

    private final InputStream in;
    private final Runnable onEnd;
    private final Consumer<IOException> onException;

    public DetectingInputStream(InputStream in, Runnable onEnd, Consumer<IOException> onException) {
        this.in = in;
        this.onEnd = onEnd;
        this.onException = onException;
    }

    @Override
    public int read() throws IOException {
        int i;
        try {
            i = in.read();
        } catch (IOException e) {
            onException.accept(e);
            throw e;
        }
        if (i == -1) {
            onEnd.run();
        }
        return i;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int i;
        try {
            i = in.read(b);
        } catch (IOException e) {
            onException.accept(e);
            throw e;
        }
        if (i == -1) {
            onEnd.run();
        }
        return i;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int i;
        try {
            i = in.read(b, off, len);
        } catch (IOException e) {
            onException.accept(e);
            throw e;
        }
        if (i == -1) {
            onEnd.run();
        }
        return i;
    }

    @Override
    public byte[] readAllBytes() throws IOException {
        try {
            return in.readAllBytes();
        } catch (IOException e) {
            onException.accept(e);
            throw e;
        }
    }

    @Override
    public byte[] readNBytes(int len) throws IOException {
        try {
            return in.readNBytes(len);
        } catch (IOException e) {
            onException.accept(e);
            throw e;
        }
    }

    @Override
    public int readNBytes(byte[] b, int off, int len) throws IOException {
        try {
            return in.readNBytes(b, off, len);
        } catch (IOException e) {
            onException.accept(e);
            throw e;
        }
    }

    @Override
    public long skip(long n) throws IOException {
        try {
            return in.skip(n);
        } catch (IOException e) {
            onException.accept(e);
            throw e;
        }
    }

    @Override
    public void skipNBytes(long n) throws IOException {
        try {
            in.skipNBytes(n);
        } catch (IOException e) {
            onException.accept(e);
            throw e;
        }
    }

    @Override
    public int available() throws IOException {
        try {
            return in.available();
        } catch (IOException e) {
            onException.accept(e);
            throw e;
        }
    }

    @Override
    public void close() throws IOException {
        try {
            in.close();
        } catch (IOException e) {
            onException.accept(e);
            throw e;
        }
    }

    @Override
    public void mark(int readlimit) {
        in.mark(readlimit);
    }

    @Override
    public void reset() throws IOException {
        try {
            in.reset();
        } catch (IOException e) {
            onException.accept(e);
            throw e;
        }
    }

    @Override
    public boolean markSupported() {
        return in.markSupported();
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        try {
            return in.transferTo(out);
        } catch (IOException e) {
            onException.accept(e);
            throw e;
        }
    }

}
