package cool.scx.tcp;

import cool.scx.tcp.tls.TLS;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * 经典 TCP Socket
 *
 * @author scx567888
 * @version 0.0.1
 */
public class ClassicTCPSocket implements ScxTCPSocket {

    private Socket socket;
    private InputStream in;
    private OutputStream out;

    public ClassicTCPSocket(Socket socket) {
        setSocket(socket);
    }

    @Override
    public InputStream inputStream() {
        return in;
    }

    @Override
    public OutputStream outputStream() {
        return out;
    }

    @Override
    public SocketAddress remoteAddress() {
        return socket.getRemoteSocketAddress();
    }

    @Override
    public SocketAddress localAddress() {
        return socket.getLocalSocketAddress();
    }

    @Override
    public ClassicTCPSocket upgradeToTLS(TLS tls) throws IOException {
        if (tls != null && tls.enabled()) {
            //创建 sslSocket (服务器端不需要设置 host 和 port)
            var sslSocket = (SSLSocket) tls.socketFactory().createSocket(socket, null, -1, true);
            sslSocket.setUseClientMode(false);
            setSocket(sslSocket);
        }
        return this;
    }

    @Override
    public boolean isTLS() {
        return socket instanceof SSLSocket;
    }

    @Override
    public ScxTCPSocket startHandshake() throws IOException {
        if (socket instanceof SSLSocket sslSocket) { 
            sslSocket.startHandshake();
        }
        return this;
    }

    @Override
    public boolean isClosed() {
        return socket.isClosed();
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    private void setSocket(Socket socket) {
        this.socket = socket;
        try {
            this.in = socket.getInputStream();
            this.out = socket.getOutputStream();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
