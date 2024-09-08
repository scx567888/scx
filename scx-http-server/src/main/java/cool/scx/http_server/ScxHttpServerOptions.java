package cool.scx.http_server;

public class ScxHttpServerOptions extends ScxTCPServerOptions {

    @Override
    public ScxHttpServerOptions setPort(int port) {
        super.setPort(port);
        return this;
    }

}
