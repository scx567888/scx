package cool.scx.http;


/**
 * ScxHttpServerOptions
 */
public class ScxHttpServerOptions {

    private int port;
    private Object tls;
    private long maxPayloadSize;

    public ScxHttpServerOptions() {
        this.port = 0;
        this.tls = null;
        this.maxPayloadSize = -1;
    }

    public int getPort() {
        return port;
    }

    public ScxHttpServerOptions setPort(int port) {
        if (port > 65535) {
            throw new IllegalArgumentException("port must be <= 65535");
        } else {
            this.port = port;
            return this;
        }
    }

    public Object getTLS() {
        return tls;
    }

    public void setTLS(Object tls) {
        this.tls = tls;
    }

    public long getMaxPayloadSize() {
        return this.maxPayloadSize;
    }

    public ScxHttpServerOptions setMaxPayloadSize(long maxPayloadSize) {
        this.maxPayloadSize = maxPayloadSize;
        return this;
    }

}
