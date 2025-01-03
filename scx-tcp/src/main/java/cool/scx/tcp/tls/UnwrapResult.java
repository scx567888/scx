package cool.scx.tcp.tls;

public final class UnwrapResult {

    public Status status;
    public int bytesConsumed;
    public int bytesProduced;

    public UnwrapResult() {
        this.status = null;
        this.bytesConsumed = 0;
        this.bytesProduced = 0;
    }

    @Override
    public String toString() {
        return "UnwrapResult[" +
                "status=" + status + ", " +
                "bytesConsumed=" + bytesConsumed + ", " +
                "bytesProduced=" + bytesProduced + ']';
    }

    public enum Status {
        SOCKET_CHANNEL_CLOSED,
        OK,
        BUFFER_OVERFLOW,
        BUFFER_UNDERFLOW,
        CLOSED
    }

}
