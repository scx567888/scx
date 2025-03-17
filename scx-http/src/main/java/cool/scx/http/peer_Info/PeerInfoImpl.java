package cool.scx.http.peer_Info;

import java.net.SocketAddress;
import java.security.Principal;
import java.security.cert.Certificate;

/// PeerInfoImpl
///
/// @author scx567888
/// @version 0.0.1
class PeerInfoImpl implements PeerInfoWritable {

    private SocketAddress socketAddress;
    private String host;
    private Integer port;
    private Principal principalSupplier;
    private Certificate[] certificateSupplier;

    @Override
    public SocketAddress address() {
        return socketAddress;
    }

    @Override
    public String host() {
        return host;
    }

    @Override
    public int port() {
        return port;
    }

    @Override
    public Principal tlsPrincipal() {
        return principalSupplier;
    }

    @Override
    public Certificate[] tlsCertificates() {
        return certificateSupplier;
    }

    @Override
    public PeerInfoWritable address(SocketAddress address) {
        this.socketAddress = address;
        return this;
    }

    @Override
    public PeerInfoWritable host(String host) {
        this.host = host;
        return this;
    }

    @Override
    public PeerInfoWritable port(int port) {
        this.port = port;
        return this;
    }

    @Override
    public PeerInfoWritable tlsPrincipal(Principal principal) {
        this.principalSupplier = principal;
        return this;
    }

    @Override
    public PeerInfoWritable tlsCertificates(Certificate[] certificates) {
        this.certificateSupplier = certificates;
        return this;
    }

}
