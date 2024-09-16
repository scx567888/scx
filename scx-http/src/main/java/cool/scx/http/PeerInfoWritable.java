package cool.scx.http;

import java.net.SocketAddress;
import java.security.Principal;
import java.security.cert.Certificate;

public interface PeerInfoWritable extends PeerInfo {

    PeerInfoWritable address(SocketAddress address);

    PeerInfoWritable host(String host);

    PeerInfoWritable port(int port);

    PeerInfoWritable tlsPrincipal(Principal principal);

    PeerInfoWritable tlsCertificates(Certificate[] certificates);
    
}
