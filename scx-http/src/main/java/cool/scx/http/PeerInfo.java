package cool.scx.http;

import java.net.SocketAddress;
import java.security.Principal;
import java.security.cert.Certificate;

/**
 * PeerInfo
 */
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
