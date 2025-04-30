package cool.scx.tcp;

import java.net.SocketAddress;

/// ScxTCPClient
///
/// @author scx567888
/// @version 0.0.1
public interface ScxTCPClient {

    /// 默认 10 秒
    int DEFAULT_TIMEOUT = 10 * 1000;

    ScxTCPSocket connect(SocketAddress endpoint, int timeout);

    default ScxTCPSocket connect(SocketAddress endpoint) {
        return connect(endpoint, DEFAULT_TIMEOUT);
    }

}
