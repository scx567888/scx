package cool.scx.http.x.http1x;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AutoContinueInputStream extends InputStream {

    private final InputStream in;
    private final OutputStream out;
    private boolean continueSent;

    public AutoContinueInputStream(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
        this.continueSent = false;
    }

    @Override
    public int read() throws IOException {
        trySendContinueResponse();
        return in.read();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        trySendContinueResponse();
        return in.read(b, off, len);
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        trySendContinueResponse();
        return in.transferTo(out);
    }

    private void trySendContinueResponse() throws IOException {
        if (!continueSent) {
            Http1xHelper.sendContinue100(out);
            continueSent = true;
        }
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

}
