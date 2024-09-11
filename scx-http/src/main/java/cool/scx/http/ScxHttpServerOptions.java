package cool.scx.http;

public class ScxHttpServerOptions {

    private int port;

    public ScxHttpServerOptions() {
        this.port = 0;
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

}
