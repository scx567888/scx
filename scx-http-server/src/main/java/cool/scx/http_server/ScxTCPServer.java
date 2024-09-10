package cool.scx.http_server;

import java.util.function.Consumer;

public interface ScxTCPServer {

    ScxTCPServer connectHandler(Consumer<ScxTCPSocket> handler);

    ScxTCPServer exceptionHandler(Consumer<Throwable> handler);

    void start();

    void stop();

}
