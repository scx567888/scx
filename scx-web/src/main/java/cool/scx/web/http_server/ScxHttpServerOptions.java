package cool.scx.web.http_server;

public class ScxHttpServerOptions {
    
    private int port;

    public ScxHttpServerOptions() {
        this.port = 0;
    }

    public int getPort() {
        return port;
    }

    public ScxHttpServerOptions setPort(int port) {
        this.port = port;
        return this;
    }
    
}
