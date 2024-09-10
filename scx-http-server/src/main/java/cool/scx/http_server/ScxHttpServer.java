package cool.scx.http_server;

import cool.scx.http_server.impl.scx.ScxHttpServerImpl;

import java.util.function.Consumer;

public interface ScxHttpServer {

    ScxHttpServer requestHandler(Consumer<ScxHttpRequest> handler);

    ScxHttpServer webSocketHandler(Consumer<ScxWebSocket> handler);

    ScxHttpServer exceptionHandler(Consumer<Throwable> handler);
    
    void start();
    
    void stop();
    
}
