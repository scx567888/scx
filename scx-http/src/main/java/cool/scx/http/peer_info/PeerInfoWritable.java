package cool.scx.http.peer_info;

import java.net.SocketAddress;
import java.security.Principal;
import java.security.cert.Certificate;

/// PeerInfoWritable
///
/// @author scx567888
/// @version 0.0.1
public interface PeerInfoWritable extends PeerInfo {

    PeerInfoWritable address(SocketAddress address);

    PeerInfoWritable host(String host);

    PeerInfoWritable port(int port);

    PeerInfoWritable tlsPrincipal(Principal principal);

    PeerInfoWritable tlsCertificates(Certificate[] certificates);

}
