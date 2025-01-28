package cool.scx.tcp;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;

/**
 * NIO TCP 客户端
 *
 * @author scx567888
 * @version 0.0.1
 */
public class AioTCPClient implements ScxTCPClient {

    private final ScxTCPClientOptions options;

    public AioTCPClient() {
        this(new ScxTCPClientOptions());
    }

    public AioTCPClient(ScxTCPClientOptions options) {
        this.options = options;
    }

    @Override
    public ScxTCPSocket connect(SocketAddress endpoint) {

        //todo 处理代理
        var proxy = options.proxy();

        AsynchronousSocketChannel socketChannel;
        try {
            socketChannel = AsynchronousSocketChannel.open();
            socketChannel.connect(endpoint).get();
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException("客户端连接失败 !!!", e);
        }

        var tcpSocket = new AioTCPSocket(socketChannel);

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
