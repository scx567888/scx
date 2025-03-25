package cool.scx.http.x.http1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static cool.scx.http.x.http1.Http1Helper.sendContinue100;

/// 当初次读取的时候 自动响应 Continue-100 响应
/// todo close 时是否也应该响应 ?
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
    public int read(byte[] b) throws IOException {
        trySendContinueResponse();
        return in.read(b);
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
            sendContinue100(out);
            continueSent = true;
        }
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

}
