package cool.scx.tcp;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

/**
 * NIO TCP 客户端
 *
 * @author scx567888
 * @version 0.0.1
 */
public class NioTCPClient implements ScxTCPClient {

    private final ScxTCPClientOptions options;

    public NioTCPClient() {
        this(new ScxTCPClientOptions());
    }

    public NioTCPClient(ScxTCPClientOptions options) {
        this.options = options;
    }

    @Override
    public ScxTCPSocket connect(SocketAddress endpoint) {

        //todo 处理代理
        var proxy = options.proxy();

        SocketChannel socketChannel;
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(endpoint);
        } catch (IOException e) {
            throw new UncheckedIOException("客户端连接失败 !!!", e);
        }

        var tcpSocket = new NioTCPSocket(socketChannel);

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

    private void tryCloseSocket(ScxTCPSocket tcpSocket, Exception e) {
        try {
            tcpSocket.close();
        } catch (IOException ex) {
            e.addSuppressed(ex);
        }
    }

}
