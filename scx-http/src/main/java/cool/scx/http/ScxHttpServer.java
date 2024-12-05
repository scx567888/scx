package cool.scx.http;

import java.util.function.Consumer;

/**
 * ScxHttpServer
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface ScxHttpServer {

    ScxHttpServer onRequest(Consumer<ScxHttpServerRequest> handler);

    void start();

    void stop();

    int port();

}
