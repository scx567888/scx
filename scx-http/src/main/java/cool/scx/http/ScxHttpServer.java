package cool.scx.http;

import cool.scx.http.web_socket.ScxServerWebSocket;

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

    //todo 这个 api 设计的不合理 因为 此种方式相当于默认允许所有握手请求 但是目前为了兼容暂时用这种方式 (应该移除只保留 onRequest)
    ScxHttpServer onWebSocket(Consumer<ScxServerWebSocket> handler);

    void start();

    void stop();

    InetSocketAddress localAddress();

}
