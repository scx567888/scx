package cool.scx.http.peer_info;

import java.net.SocketAddress;
import java.security.Principal;
import java.security.cert.Certificate;

/// PeerInfo
///
/// @author scx567888
/// @version 0.0.1
public interface PeerInfo {

    static PeerInfoWritable of() {
        return new PeerInfoImpl();
    }

    SocketAddress address();

    String host();

    int port();

    Principal tlsPrincipal();

    Certificate[] tlsCertificates();

}
