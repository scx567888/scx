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

            if (tls != null && tls.enabled()) {
                //创建 sslEngine
                var sslEngine = tls.sslContext().createSSLEngine();
                sslEngine.setUseClientMode(true);
                //创建 SocketChannel
                var socketChannel = SocketChannel.open();
                socketChannel.connect(endpoint);
                //创建 TLSScxTCPSocketImpl 并执行握手
                var socket = new TLSScxTCPSocketImpl(socketChannel, sslEngine);
                socket.startHandshake();
                return socket;
            } else {
                var socketChannel = SocketChannel.open();
                socketChannel.connect(endpoint);
                return new ScxTCPSocketImpl(socketChannel);
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
