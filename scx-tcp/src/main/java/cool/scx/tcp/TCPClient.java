package cool.scx.tcp;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.net.SocketAddress;

/// 经典 TCP 客户端
///
/// @author scx567888
/// @version 0.0.1
public class TCPClient implements ScxTCPClient {

    private final TCPClientOptions options;

    public TCPClient() {
        this(new TCPClientOptions());
    }

    public TCPClient(TCPClientOptions options) {
        this.options = options;
    }

    @Override
    public ScxTCPSocket connect(SocketAddress endpoint, int timeout) {

        var socket = new Socket();
        try {
            socket.connect(endpoint, timeout);
        } catch (IOException e) {
            throw new UncheckedIOException("客户端连接失败 !!!", e);
        }

        var tcpSocket = new TCPSocket(socket);

        if (options.autoUpgradeToTLS()) {
            try {
                tcpSocket.upgradeToTLS(options.tls());
            } catch (IOException e) {
                tryCloseSocket(tcpSocket, e);
                throw new UncheckedIOException("升级到 TLS 时发生错误 !!!", e);
            }
        }

        if (tcpSocket.tlsManager() != null) {
            tcpSocket.tlsManager().setUseClientMode(true);
        }

        if (options.autoHandshake()) {
            try {
                tcpSocket.startHandshake();
            } catch (IOException e) {
                tryCloseSocket(tcpSocket, e);
                throw new UncheckedIOException("处理 TLS 握手 时发生错误 !!!", e);
            }
        }

        return tcpSocket;

    }

    public TCPClientOptions options() {
        return options;
    }

    private void tryCloseSocket(ScxTCPSocket tcpSocket, Exception e) {
        try {
            tcpSocket.close();
        } catch (IOException ex) {
            e.addSuppressed(ex);
        }
    }

}
