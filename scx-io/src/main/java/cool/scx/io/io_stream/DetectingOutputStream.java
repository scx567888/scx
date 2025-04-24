package cool.scx.io.io_stream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;

public class DetectingOutputStream extends OutputStream {

    private final OutputStream out;
    private final Consumer<IOException> onException;

    public DetectingOutputStream(OutputStream out, Consumer<IOException> onException) {
        this.out = out;
        this.onException = onException;
    }

    @Override
    public void write(int b) throws IOException {
        try {
            out.write(b);
        } catch (IOException e) {
            onException.accept(e);
            throw e;
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        try {
            out.write(b);
        } catch (IOException e) {
            onException.accept(e);
            throw e;
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        try {
            out.write(b, off, len);
        } catch (IOException e) {
            onException.accept(e);
            throw e;
        }
    }

    @Override
    public void flush() throws IOException {
        try {
            out.flush();
        } catch (IOException e) {
            onException.accept(e);
            throw e;
        }
    }

    @Override
    public void close() throws IOException {
        try {
            out.close();
        } catch (IOException e) {
            onException.accept(e);
            throw e;
        }
    }

}
