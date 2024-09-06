package cool.scx.web.http_server;

import java.util.function.Consumer;

public interface ScxHttpServer {
    
    void requestHandler(Consumer<ScxHttpRequest> handler);
    
    void webSocketHandler(Consumer<ScxWebSocket> handler);

    void exceptionHandler(Consumer<Throwable> handler);
    
    void start();
    
    void stop();
    
}
