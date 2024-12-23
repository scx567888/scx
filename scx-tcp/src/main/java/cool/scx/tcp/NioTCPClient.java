package cool.scx.tcp;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

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
        var tls = options.tls();

        //todo 处理代理
        var proxy = options.proxy();

        SocketChannel socketChannel;
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(endpoint);
        } catch (IOException e) {
            throw new UncheckedIOException("客户端连接失败 !!!", e);
        }

        var socket = createScxTCPSocket(socketChannel);

        // 主动调用握手 快速检测 SSL 错误 防止等到调用用户处理程序时才发现 
        if (socket instanceof NioTLSTCPSocket sslSocket) {
            try {
                sslSocket.startHandshake();
            } catch (IOException e) {
                try {
                    sslSocket.close();
                } catch (IOException ce) {
                    e.addSuppressed(ce);
                }
                throw new UncheckedIOException("客户端 SSL 握手失败 !!!", e);
            }
        }

        return socket;

    }

    private ScxTCPSocket createScxTCPSocket(SocketChannel socketChannel) {
        var tls = options.tls();

        if (tls != null && tls.enabled()) {
            //创建 sslEngine
            var sslEngine = tls.sslContext().createSSLEngine();
            sslEngine.setUseClientMode(true);
            return new NioTLSTCPSocket(socketChannel, sslEngine);
        } else {
            return new NioTCPSocket(socketChannel);
        }
    }

}
