package cool.scx.http;

import cool.scx.http.error_handler.ScxHttpServerErrorHandler;

import java.net.InetSocketAddress;
import java.util.function.Consumer;

/// ScxHttpServer
///
/// @author scx567888
/// @version 0.0.1
public interface ScxHttpServer {

    ScxHttpServer onRequest(Consumer<ScxHttpServerRequest> requestHandler);

    ScxHttpServer onError(ScxHttpServerErrorHandler errorHandler);

    void start();

    void stop();

    InetSocketAddress localAddress();

}
