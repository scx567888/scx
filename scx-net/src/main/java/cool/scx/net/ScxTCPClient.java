package cool.scx.net;

import java.net.Socket;
import java.net.SocketAddress;

public interface ScxTCPClient {

    Socket connect(SocketAddress endpoint);

}
