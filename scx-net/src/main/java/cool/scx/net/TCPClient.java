package cool.scx.net;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.net.SocketAddress;

public class TCPClient implements ScxTCPClient {

    private final ScxTCPClientOptions options;

    public TCPClient() {
        this(new ScxTCPClientOptions());
    }

    public TCPClient(ScxTCPClientOptions options) {
        this.options = options;
    }

    @Override
    public ScxTCPSocket connect(SocketAddress endpoint) {
        try {
            var tls = options.tls();

            //todo 处理代理
            var proxy = options.proxy();

            Socket socket;
            if (tls != null && tls.enabled()) {
                socket = tls.createSocket();
            } else {
                socket = new Socket();
            }

            socket.connect(endpoint);

            if (socket instanceof SSLSocket sslSocket) {
                sslSocket.startHandshake();
            }

            return new TCPSocket(socket);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
