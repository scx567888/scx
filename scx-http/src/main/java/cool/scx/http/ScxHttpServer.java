package cool.scx.http;

import java.net.InetSocketAddress;
import java.util.function.Consumer;

/**
 * ScxHttpServer
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface ScxHttpServer {

    ScxHttpServer onRequest(Consumer<ScxHttpServerRequest> requestHandler);

    void start();

    void stop();

    InetSocketAddress localAddress();

}
