package cool.scx.net;

import java.net.SocketAddress;

public interface ScxTCPClient {

    ScxTCPSocket connect(SocketAddress endpoint);

}
