package cool.scx.tcp;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.function.Consumer;

/// ScxTCPServer
///
/// @author scx567888
/// @version 0.0.1
public interface ScxTCPServer {

    /// 默认 128
    int DEFAULT_BACKLOG = 128;

    ScxTCPServer onConnect(Consumer<ScxTCPSocket> connectHandler);

    void start(SocketAddress localAddress, int backlog);

    void stop();

    InetSocketAddress localAddress();

    default void start(int port) {
        start(new InetSocketAddress(port), DEFAULT_BACKLOG);
    }

    default void start(int port, int backlog) {
        start(new InetSocketAddress(port), backlog);
    }

}
