package cool.scx.tcp;

import cool.scx.tcp.tls.TLS;
import cool.scx.tcp.tls.TLSSocketChannel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;

/**
 * NIO TCP Socket
 *
 * @author scx567888
 * @version 0.0.1
 */
public class NioTCPSocket implements ScxTCPSocket {

    private SocketChannel socket;
    private InputStream in;
    private OutputStream out;
    private ScxTLSManager tlsManager;

    public NioTCPSocket(SocketChannel socket) {
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
    public InetSocketAddress remoteAddress() {
        try {
            return (InetSocketAddress) socket.getRemoteAddress();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public InetSocketAddress localAddress() {
        try {
            return (InetSocketAddress) socket.getLocalAddress();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public ScxTCPSocket upgradeToTLS(TLS tls) throws IOException {
        if (tls != null && tls.enabled()) {
            //创建 sslEngine
            var sslSocket = new TLSSocketChannel(socket, tls.sslContext().createSSLEngine());
            setSocket(sslSocket);
        }
        return this;
    }

    @Override
    public boolean isTLS() {
        return socket instanceof TLSSocketChannel;
    }

    @Override
    public ScxTCPSocket startHandshake() throws IOException {
        if (socket instanceof TLSSocketChannel tlsSocketChannel) {
            tlsSocketChannel.startHandshake();
        }
        return this;
    }

    @Override
    public ScxTLSManager tlsManager() {
        return tlsManager;
    }

    @Override
    public boolean isClosed() {
        return !socket.isOpen();
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    private void setSocket(SocketChannel socket) {
        this.socket = socket;
        this.in = Channels.newInputStream(socket);
        this.out = Channels.newOutputStream(socket);
        if (socket instanceof TLSSocketChannel tlsSocketChannel) {
            tlsManager = new NioTLSManager(tlsSocketChannel.sslEngine());
        }
    }

}
