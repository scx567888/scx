package cool.scx.net;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

public class NioScxTCPClientImpl implements ScxTCPClient {
    
    private final ScxTCPClientOptions options;

    public NioScxTCPClientImpl() {
        this(new ScxTCPClientOptions());
    }

    public NioScxTCPClientImpl(ScxTCPClientOptions options) {
        this.options = options;
    }

    @Override
    public ScxTCPSocket connect(SocketAddress endpoint) {
        try {
            //todo 处理 SSL
            var tls = options.tls();

            //todo 处理代理
            var proxy = options.proxy();

            SocketChannel socket=SocketChannel.open();
//            if (tls != null && tls.enabled()) {
//                socket = tls.createSocket();
//            } else {
//                socket = new Socket();
//            }

            socket.connect(endpoint);

            return new NioScxTCPSocketImpl(socket);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
