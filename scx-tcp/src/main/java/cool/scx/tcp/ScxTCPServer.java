package cool.scx.tcp;

import cool.scx.functional.ScxConsumer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/// TCP 服务器
///
/// @author scx567888
/// @version 0.0.1
public interface ScxTCPServer {

    ScxTCPServer onConnect(ScxConsumer<ScxTCPSocket, ?> connectHandler);

    void start(SocketAddress localAddress) throws IOException;

    void stop();

    InetSocketAddress localAddress();

    default void start(int port) throws IOException {
        start(new InetSocketAddress(port));
    }

}
