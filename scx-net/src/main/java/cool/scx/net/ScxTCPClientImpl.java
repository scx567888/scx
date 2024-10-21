package cool.scx.net;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

public class ScxTCPClientImpl implements ScxTCPClient {

    private final ScxTCPClientOptions options;

    public ScxTCPClientImpl() {
        this(new ScxTCPClientOptions());
    }

    public ScxTCPClientImpl(ScxTCPClientOptions options) {
        this.options = options;
    }

    @Override
    public ScxTCPSocket connect(SocketAddress endpoint) {
        try {
            var tls = options.tls();

            //todo 处理代理
            var proxy = options.proxy();

            var socketChannel = SocketChannel.open();
            ScxTCPSocket socket;
            if (tls != null && tls.enabled()) {
                socket = new TLSScxTCPSocketImpl(socketChannel, tls.sslContext().createSSLEngine());
            } else {
                socket = new ScxTCPSocketImpl(socketChannel);
            }

            socketChannel.connect(endpoint);

            return socket;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
