package cool.scx.tcp;

import cool.scx.io.io_stream.DetectingInputStream;
import cool.scx.io.io_stream.DetectingOutputStream;
import cool.scx.tcp.tls.TLS;
import cool.scx.tcp.tls_channel.TLSSocketChannel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import static java.nio.channels.Channels.newInputStream;
import static java.nio.channels.Channels.newOutputStream;

/// NIO TCP Socket
///
/// @author scx567888
/// @version 0.0.1
public class NioTCPSocket implements ScxTCPSocket {

    private SocketChannel socketChannel;
    private InputStream in;
    private OutputStream out;
    private ScxTLSManager tlsManager;
    private Runnable closeHandler;
    private boolean remoteClosed = false;

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
            setSocket(sslSocket);
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
    public ScxTCPSocket onClose(Runnable closeHandler) {
        this.closeHandler = closeHandler;
        return this;
    }

    @Override
    public void close() throws IOException {
        socketChannel.close();
    }

    private void setSocket(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        this.in = new DetectingInputStream(newInputStream(socketChannel), this::inputEnd, this::inputException);
        this.out = new DetectingOutputStream(newOutputStream(socketChannel), this::outputException);
        if (socketChannel instanceof TLSSocketChannel tlsSocketChannel) {
            tlsManager = new NioTLSManager(tlsSocketChannel.sslEngine());
        }
    }

    private void inputEnd() {
        callOnRemoteClose();
    }

    private void inputException(IOException e) {
        callOnRemoteClose();
    }

    private void outputException(IOException e) {
        callOnRemoteClose();
    }

    private void callOnRemoteClose() {
        if (remoteClosed) {
            return;
        }
        remoteClosed = true;
        if (closeHandler != null) {
            closeHandler.run();
        }
    }

}
