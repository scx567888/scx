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

    /// 只在连接可能可用时调用, 若远端断开连接,或其他网络中断类错误 则不会触发
    ScxHttpServer onError(ScxHttpServerErrorHandler errorHandler);

    void start();

    void stop();

    InetSocketAddress localAddress();

}
