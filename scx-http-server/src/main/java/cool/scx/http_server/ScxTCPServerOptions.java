package cool.scx.http_server;

public class ScxTCPServerOptions {

    private int port;

    public ScxTCPServerOptions() {
        this.port = 0;
    }

    public int getPort() {
        return port;
    }

    public ScxTCPServerOptions setPort(int port) {
        if (port > 65535) {
            throw new IllegalArgumentException("port must be <= 65535");
        } else {
            this.port = port;
            return this;
        }
    }

}
