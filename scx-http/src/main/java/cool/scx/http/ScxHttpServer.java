package cool.scx.http;

import cool.scx.http.web_socket.ScxServerWebSocket;

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

    int port();

}
