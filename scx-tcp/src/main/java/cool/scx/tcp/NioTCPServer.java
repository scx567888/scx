package cool.scx.tcp;

import cool.scx.tcp.tls.TLSSocketChannel;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.System.Logger;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.function.Consumer;

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.TRACE;

/**
 * NIO TCP 服务器
 *
 * @author scx567888
 * @version 0.0.1
 */
public class NioTCPServer implements ScxTCPServer {

    private static final Logger LOGGER = System.getLogger(ClassicTCPServer.class.getName());

    private final ScxTCPServerOptions options;
    private final Thread serverThread;
    private Consumer<ScxTCPSocket> connectHandler;
    private ServerSocketChannel serverSocketChannel;
    private boolean running;

    public NioTCPServer() {
        this(new ScxTCPServerOptions());
    }

    public NioTCPServer(ScxTCPServerOptions options) {
        this.options = options;
        this.serverThread = Thread.ofPlatform().name("NioTCPServer-Listener").unstarted(this::listen);
    }

    @Override
    public ScxTCPServer onConnect(Consumer<ScxTCPSocket> connectHandler) {
        this.connectHandler = connectHandler;
        return this;
    }

    @Override
    public void start() {
        if (running) {
            throw new IllegalStateException("服务器已在运行 !!!");
        }

        try {
            this.serverSocketChannel = ServerSocketChannel.open();
            this.serverSocketChannel.bind(options.localAddress(), options.backlog());
        } catch (IOException e) {
            throw new UncheckedIOException("启动服务器失败 !!!", e);
        }

        running = true;

        serverThread.start();
    }

    @Override
    public void stop() {
        if (!running) {
            return;
        }

        running = false;

        try {
            serverSocketChannel.close();
        } catch (IOException e) {
            throw new UncheckedIOException("关闭服务器失败 !!!", e);
        }

        serverThread.interrupt();
    }

    @Override
    public InetSocketAddress localAddress() {
        try {
            return (InetSocketAddress) serverSocketChannel.getLocalAddress();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void listen() {
        while (running) {
            try {
                var socketChannel = this.serverSocketChannel.accept();
                Thread.ofVirtual().name("NioTCPServer-Handler-" + socketChannel.getRemoteAddress()).start(() -> handle(socketChannel));
            } catch (IOException e) {
                LOGGER.log(ERROR, "服务器 接受连接 时发生错误 !!!", e);
                stop();
            }
        }
    }

    private void handle(SocketChannel socketChannel) {

        try {
            socketChannel = upgradeToTLS(socketChannel);
        } catch (Exception e) {
            LOGGER.log(TRACE, "升级到 TLS 时发生错误 !!!", e);
            tryCloseSocket(socketChannel);
            return;
        }

        try {
            // 主动调用握手 快速检测 SSL 错误 防止等到调用用户处理程序时才发现
            if (socketChannel instanceof TLSSocketChannel sslSocket) {
                sslSocket.startHandshake();
            }
        } catch (IOException e) {
            LOGGER.log(TRACE, "处理 TLS 握手 时发生错误 !!!", e);
            tryCloseSocket(socketChannel);
            return;
        }

        if (connectHandler == null) {
            LOGGER.log(ERROR, "未设置 连接处理器, 关闭连接 !!!");
            tryCloseSocket(socketChannel);
            return;
        }

        try {
            // 调用用户处理器
            var tcpSocket = new NioTCPSocket(socketChannel);
            connectHandler.accept(tcpSocket);
        } catch (Throwable e) {
            LOGGER.log(ERROR, "调用 连接处理器 时发生错误 !!!", e);
            tryCloseSocket(socketChannel);
        }

    }

    private void tryCloseSocket(SocketChannel socketChannel) {
        try {
            socketChannel.close();
        } catch (IOException ex) {
            LOGGER.log(TRACE, "关闭 Socket 时发生错误 !!!", ex);
        }
    }

    private SocketChannel upgradeToTLS(SocketChannel socketChannel) {
        var tls = options.tls();

        if (tls != null && tls.enabled()) {
            //创建 sslEngine
            var sslEngine = tls.sslContext().createSSLEngine();
            sslEngine.setUseClientMode(false);
            return new TLSSocketChannel(socketChannel, sslEngine);
        } else {
            return socketChannel;
        }
    }

}
