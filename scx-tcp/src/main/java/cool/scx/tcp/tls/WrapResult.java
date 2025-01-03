package cool.scx.tcp.tls;

import javax.net.ssl.SSLEngineResult.Status;

public final class WrapResult {

    public Status status;
    public int bytesConsumed;
    public int bytesProduced;

    public WrapResult() {
        this.status = null;
        this.bytesConsumed = 0;
        this.bytesProduced = 0;
    }

    @Override
    public String toString() {
        return "WrapResult[" +
                "status=" + status + ", " +
                "bytesConsumed=" + bytesConsumed + ", " +
                "bytesProduced=" + bytesProduced + ']';
    }

}
