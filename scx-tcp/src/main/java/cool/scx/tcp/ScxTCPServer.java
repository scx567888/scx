package cool.scx.tcp;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.function.Consumer;

/// ScxTCPServer
///
/// @author scx567888
/// @version 0.0.1
public interface ScxTCPServer {

    ScxTCPServer onConnect(Consumer<ScxTCPSocket> connectHandler);

    void start(SocketAddress localAddress);

    void stop();

    InetSocketAddress localAddress();

    default void start(int port) {
        start(new InetSocketAddress(port));
    }

}
