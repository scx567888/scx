package cool.scx.tcp;

import java.net.SocketAddress;


/**
 * ScxTCPClient
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface ScxTCPClient {

    ScxTCPSocket connect(SocketAddress endpoint);

}