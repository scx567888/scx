package cool.scx.tcp;

import cool.scx.tcp.tls.TLS;
import cool.scx.tcp.tls.TLSSocketChannel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.SocketAddress;
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
    private ScxTLSConfig tlsConfig;

    public NioTCPSocket(SocketChannel socketChannel) {
        setSocket(socketChannel);
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
        try {
            return socketChannel.getRemoteAddress();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public SocketAddress localAddress() {
        try {
            return socketChannel.getLocalAddress();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public ScxTCPSocket upgradeToTLS(TLS tls) throws IOException {
        if (tls != null && tls.enabled()) {
            //创建 sslEngine
            var sslEngine = tls.sslContext().createSSLEngine();
            sslEngine.setUseClientMode(false);
            setSocket(new TLSSocketChannel(socketChannel, sslEngine));
            tlsConfig = new NioTLSConfig(sslEngine);
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
    public boolean isClosed() {
        return !socketChannel.isOpen();
    }

    @Override
    public ScxTLSConfig tlsConfig() {
        return tlsConfig;
    }

    @Override
    public void close() throws IOException {
        socketChannel.close();
    }

    private void setSocket(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        this.in = Channels.newInputStream(socketChannel);
        this.out = Channels.newOutputStream(socketChannel);
    }

}
