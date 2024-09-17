package cool.scx.http;

import java.util.function.Consumer;

/**
 * ScxHttpServer
 */
public interface ScxHttpServer {

    ScxHttpServer requestHandler(Consumer<ScxHttpServerRequest> handler);

    ScxHttpServer webSocketHandler(Consumer<ScxServerWebSocket> handler);

    ScxHttpServer errorHandler(Consumer<Throwable> handler);

    void start();

    void stop();

}
