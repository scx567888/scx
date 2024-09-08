package cool.scx.http_server;

import cool.scx.http_server.impl.ScxTCPServerImpl;

import java.util.function.Consumer;

public interface ScxTCPServer {

    static ScxTCPServer create() {
        return create(new ScxTCPServerOptions());
    }

    static ScxTCPServer create(ScxTCPServerOptions options) {
        return new ScxTCPServerImpl(options);
    }

    ScxTCPServer connectHandler(Consumer<ScxTCPSocket> handler);

    ScxTCPServer exceptionHandler(Consumer<Throwable> handler);

    void start();

    void stop();

}
