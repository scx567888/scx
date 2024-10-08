package cool.scx.http.helidon;

import io.helidon.common.socket.PeerInfo;

import java.net.SocketAddress;
import java.security.Principal;
import java.security.cert.Certificate;

/**
 * HelidonPeerInfo
 */
public class HelidonPeerInfo implements cool.scx.http.PeerInfo {

    private final PeerInfo peerInfo;

    public HelidonPeerInfo(PeerInfo peerInfo) {
        this.peerInfo = peerInfo;
    }

    @Override
    public SocketAddress address() {
        return peerInfo.address();
    }

    @Override
    public String host() {
        return peerInfo.host();
    }

    @Override
    public int port() {
        return peerInfo.port();
    }

    @Override
    public Principal tlsPrincipal() {
        return peerInfo.tlsPrincipal().orElse(null);
    }

    @Override
    public Certificate[] tlsCertificates() {
        return peerInfo.tlsCertificates().orElse(null);
    }

}
