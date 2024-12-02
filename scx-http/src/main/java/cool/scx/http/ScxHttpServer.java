package cool.scx.http;

import cool.scx.http.web_socket.ScxServerWebSocket;

import java.util.function.Consumer;

/**
 * ScxHttpServer
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface ScxHttpServer {

    ScxHttpServer requestHandler(Consumer<ScxHttpServerRequest> handler);

    ScxHttpServer webSocketHandler(Consumer<ScxServerWebSocket> handler);

    ScxHttpServer errorHandler(Consumer<Throwable> handler);

    void start();

    void stop();

    int port();

}
