package cool.scx.http_server;

import cool.scx.http_server.impl.ScxHttpServerImpl;

import java.util.function.Consumer;

public interface ScxHttpServer {

    static ScxHttpServer create() {
        return create(new ScxHttpServerOptions());
    }

    static ScxHttpServer create(ScxHttpServerOptions options) {
        return new ScxHttpServerImpl(options);
    }

    ScxHttpServer requestHandler(Consumer<ScxHttpRequest> handler);

    ScxHttpServer webSocketHandler(Consumer<ScxWebSocket> handler);

    ScxHttpServer exceptionHandler(Consumer<Throwable> handler);
    
    void start();
    
    void stop();
    
}
