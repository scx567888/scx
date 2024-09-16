package cool.scx.http;

import java.util.function.Consumer;

public interface ScxHttpServer {

    ScxHttpServer requestHandler(Consumer<ScxHttpServerRequest> handler);

    ScxHttpServer webSocketHandler(Consumer<ScxServerWebSocket> handler);

    ScxHttpServer exceptionHandler(Consumer<Throwable> handler);

    void start();

    void stop();

}
