package cool.scx.http.x.http1x;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * NoCloseOutputStream
 *
 * @author scx567888
 * @version 0.0.1
 */
public class NoCloseOutputStream extends OutputStream {

    private final OutputStream out;

    public NoCloseOutputStream(OutputStream out) {
        this.out=out;
    }

    @Override
    public void write(int b) throws IOException {
        this.out.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.out.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.out.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        this.out.flush();
    }

    @Override
    public void close() {
        //这里什么也不做
        System.out.println();
    }

}
