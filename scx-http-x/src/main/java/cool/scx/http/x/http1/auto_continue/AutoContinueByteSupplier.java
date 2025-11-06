package cool.scx.http.x.http1.auto_continue;

import cool.scx.io.ByteChunk;
import cool.scx.io.ByteOutput;
import cool.scx.io.supplier.ByteSupplier;

import java.io.IOException;

import static cool.scx.http.x.http1.Http1Helper.sendContinue100;

/// 当初次读取的时候 自动响应 Continue-100 响应
/// todo close 时是否也应该响应 ?
public class AutoContinueByteSupplier implements ByteSupplier {

    private final ByteSupplier in;
    private final ByteOutput out;
    private boolean continueSent;

    public AutoContinueByteSupplier(ByteSupplier byteSupplier, ByteOutput out) {
        this.in = byteSupplier;
        this.out = out;
        this.continueSent = false;
    }

    @Override
    public ByteChunk get() throws Exception {
        trySendContinueResponse();
        return in.get();
    }

    @Override
    public void close() throws Exception {
        in.close();
    }

    private void trySendContinueResponse() throws IOException {
        if (!continueSent) {
            sendContinue100(out);
            continueSent = true;
        }
    }

}
