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

    private SocketChannel socketChannel;
    private InputStream in;
    private OutputStream out;
    private ScxTLSManager tlsManager;

    public NioTCPSocket(SocketChannel socketChannel) {
        setSocketChannel(socketChannel);
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
            return (InetSocketAddress) socketChannel.getRemoteAddress();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public InetSocketAddress localAddress() {
        try {
            return (InetSocketAddress) socketChannel.getLocalAddress();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public ScxTCPSocket upgradeToTLS(TLS tls) throws IOException {
        if (tls != null && tls.enabled()) {
            //创建 sslEngine
            var sslSocket = new TLSSocketChannel(socketChannel, tls.sslContext().createSSLEngine());
            setSocketChannel(sslSocket);
        }
        return this;
    }

    @Override
    public boolean isTLS() {
        return socketChannel instanceof TLSSocketChannel;
    }

    @Override
    public ScxTCPSocket startHandshake() throws IOException {
        if (socketChannel instanceof TLSSocketChannel tlsSocketChannel) {
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
        return !socketChannel.isOpen();
    }

    @Override
    public void close() throws IOException {
        socketChannel.close();
    }

    private void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        this.in = Channels.newInputStream(socketChannel);
        this.out = Channels.newOutputStream(socketChannel);
        if (socketChannel instanceof TLSSocketChannel tlsSocketChannel) {
            tlsManager = new NioTLSManager(tlsSocketChannel.sslEngine());
        }
    }

}
