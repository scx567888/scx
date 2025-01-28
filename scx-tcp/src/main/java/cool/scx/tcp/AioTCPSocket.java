package cool.scx.tcp;

import cool.scx.tcp.tls.TLS;
import cool.scx.tcp.tls.TLSAsynchronousSocketChannel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.Channels;

/**
 * 异步 I/O TCP 套接字
 *
 * @author scx567888
 * @version 0.0.1
 */
public class AioTCPSocket implements ScxTCPSocket {

    private AsynchronousSocketChannel socketChannel;
    private InputStream in;
    private OutputStream out;
    private ScxTLSManager tlsManager;

    public AioTCPSocket(AsynchronousSocketChannel socketChannel) {
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
            var sslSocket = new TLSAsynchronousSocketChannel(socketChannel, tls.sslContext().createSSLEngine());
            setSocket(sslSocket);
        }
        return this;
    }

    @Override
    public boolean isTLS() {
        return socketChannel instanceof TLSAsynchronousSocketChannel;
    }

    @Override
    public ScxTCPSocket startHandshake() throws IOException {
        if (socketChannel instanceof TLSAsynchronousSocketChannel tlsSocketChannel) {
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

    private void setSocket(AsynchronousSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        this.in = Channels.newInputStream(socketChannel);
        this.out = Channels.newOutputStream(socketChannel);
        if (socketChannel instanceof TLSAsynchronousSocketChannel tlsSocketChannel) {
            tlsManager = new NioTLSManager(tlsSocketChannel.sslEngine());
        }
    }

}
