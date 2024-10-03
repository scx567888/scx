package cool.scx.http;


/**
 * ScxHttpServerOptions
 */
public class ScxHttpServerOptions {

    private int port;
    private Object tls;
    private long maxPayloadSize;
    private int bodyBufferSize;

    public ScxHttpServerOptions() {
        this.port = 0;
        this.tls = null;
        this.maxPayloadSize = -1;
        this.bodyBufferSize = 65536;
    }

    public int port() {
        return port;
    }

    public ScxHttpServerOptions port(int port) {
        if (port > 65535) {
            throw new IllegalArgumentException("port must be <= 65535");
        } else {
            this.port = port;
            return this;
        }
    }

    public Object tls() {
        return tls;
    }

    public void tls(Object tls) {
        this.tls = tls;
    }

    public long maxPayloadSize() {
        return this.maxPayloadSize;
    }

    public ScxHttpServerOptions maxPayloadSize(long maxPayloadSize) {
        this.maxPayloadSize = maxPayloadSize;
        return this;
    }

    public int bodyBufferSize() {
        return bodyBufferSize;
    }

    public ScxHttpServerOptions bodyBufferSize(int bodyBufferSize) {
        this.bodyBufferSize = bodyBufferSize;
        return this;
    }

}
