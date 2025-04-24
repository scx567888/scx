package cool.scx.tcp;

import cool.scx.io.io_stream.DetectingInputStream;
import cool.scx.io.io_stream.DetectingOutputStream;
import cool.scx.tcp.tls.TLS;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/// 经典 TCP Socket
///
/// @author scx567888
/// @version 0.0.1
public class ClassicTCPSocket implements ScxTCPSocket {

    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private ScxTLSManager tlsManager;
    private Runnable closeHandler;
    private boolean remoteClosed = false;

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
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress) socket.getRemoteSocketAddress();
    }

    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress) socket.getLocalSocketAddress();
    }

    @Override
    public ClassicTCPSocket upgradeToTLS(TLS tls) throws IOException {
        if (tls != null && tls.enabled()) {
            //创建 sslSocket (服务器端不需要设置 host 和 port)
            var sslSocket = tls.socketFactory().createSocket(socket, null, -1, true);
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
    public ScxTLSManager tlsManager() {
        return tlsManager;
    }

    @Override
    public boolean isClosed() {
        return socket.isClosed();
    }

    @Override
    public ScxTCPSocket onClose(Runnable closeHandler) {
        this.closeHandler = closeHandler;
        return this;
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    private void setSocket(Socket socket) {
        this.socket = socket;
        try {
            this.in = new DetectingInputStream(socket.getInputStream(), this::inputEnd, this::inputException);
            this.out = new DetectingOutputStream(socket.getOutputStream(), this::outputException);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        if (socket instanceof SSLSocket sslSocket) {
            tlsManager = new ClassicTLSManager(sslSocket);
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
