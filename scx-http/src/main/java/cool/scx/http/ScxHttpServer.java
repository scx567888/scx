package cool.scx.http;

import cool.scx.function.Function1Void;
import cool.scx.http.error_handler.ScxHttpServerErrorHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/// ScxHttpServer
///
/// @author scx567888
/// @version 0.0.1
public interface ScxHttpServer {

    ScxHttpServer onRequest(Function1Void<ScxHttpServerRequest, ?> requestHandler);

    /// 只在连接可能可用时调用, 若远端断开连接,或其他网络中断类错误 则不会触发
    ScxHttpServer onError(ScxHttpServerErrorHandler errorHandler);

    void start(SocketAddress localAddress) throws IOException;

    void stop();

    InetSocketAddress localAddress();

    default void start(int port) throws IOException {
        start(new InetSocketAddress(port));
    }

}
