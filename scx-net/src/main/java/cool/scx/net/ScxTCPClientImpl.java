package cool.scx.net;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.net.SocketAddress;

public class ScxTCPClientImpl implements ScxTCPClient {

    private final ScxTCPClientOptions options;

    public ScxTCPClientImpl() {
        this(new ScxTCPClientOptions());
    }

    public ScxTCPClientImpl(ScxTCPClientOptions options) {
        this.options = options;
    }

    @Override
    public Socket connect(SocketAddress endpoint) {
        try {
            var tls = options.tls();

            Socket socket;
            if (tls != null && tls.enabled()) {
                socket = tls.createSocket();
            } else {
                socket = new Socket();
            }

            socket.connect(endpoint);
            return socket;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
