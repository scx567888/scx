package cool.scx.net;

import java.net.Socket;
import java.net.SocketAddress;

public interface ScxTCPClient {

    ScxTCPSocket connect(SocketAddress endpoint);

}
